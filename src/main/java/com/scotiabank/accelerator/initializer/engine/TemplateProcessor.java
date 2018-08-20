package com.scotiabank.accelerator.initializer.engine;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.scotiabank.accelerator.initializer.controller.request.ComponentAddRequest;
import com.scotiabank.accelerator.initializer.core.zip.ZipFile;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static java.nio.file.Files.*;

@Component
@Slf4j
public class TemplateProcessor {

    private final Mustache.Compiler mustache;
    private final String sourceTemplatePath;
    private final ZipFile zipFile;

    public TemplateProcessor(Mustache.Compiler mustache,
                             @Value("${initializer.template-path}") String sourceTemplatePath,
                             ZipFile zipFile) {
        this.mustache = mustache;
        this.sourceTemplatePath = sourceTemplatePath;
        this.zipFile = zipFile;
    }

    /**
     * Create a new project based on an existing template, customize the values with Mustache template engine and provide in the component input param
     *
     * @param component {@link ComponentAddRequest} attributes to create a new project
     * @return A byte[] containing the new project zipped.
     * @throws IOException if the path could not be deleted.
     */
    public byte[] createApplication(ComponentAddRequest component) throws InvalidTemplateException, URISyntaxException, IOException {
        log.debug("Start the creation of application '{}' with template '{}'", component.getName(), component.getType());

        log.debug("Locate the source directory of the template");
        URI sourceTemplateDirectory = getTemplatePath(component.getType());
        log.trace("Source template directory is {}", sourceTemplateDirectory.getPath());

        log.debug("Generating destination directory");
        final Path destinationPath = createDestinationDirectory(component.getName());
        log.trace("Destination template directory is {}", destinationPath);

        // Candidate to be in a future manifest
        String javaApplicationName = WordUtils.capitalizeFully(component.getName(), '.', ' ', '-');
        javaApplicationName = javaApplicationName.replaceAll("[\\.\\-]", "");
        component.setJavaApplicationName(javaApplicationName);
        component.setJavaPackageName(component.getName().replaceAll("[\\.\\-]", ""));

        log.debug("Traverse directory and process each template files");
        try (Stream<Path> paths = walk(Paths.get(sourceTemplateDirectory))) {
            paths
                    .forEach(currentPath -> {
                        log.debug("Current path to process: {}", currentPath);
                        String relativePath = getRelativePath(component, sourceTemplateDirectory, currentPath);

                        process(component, destinationPath, currentPath, relativePath);
                    });
        } catch (IOException e) {
            log.error("Something went wrong while traversing the source template directory", e);
        }

        log.debug("Zipping the created template from temporary directory");
        byte[] content = readAllBytes(zipFile.zip(destinationPath.toString()).toPath());

        log.debug("Delete temporary directory");
        deleteDirectoryAndContent(StringUtils.removeEnd(destinationPath.toString(), component.getName()));
        return content;
    }

    /**
     * Delete a directory and all its content recursively.
     *
     * @param path The path of the directory to delete
     * @throws IOException if the path could not be deleted.
     */
    private void deleteDirectoryAndContent(String path) throws IOException {
        try (Stream<Path> paths = walk(Paths.get(path))) {
            paths
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    /**
     * Processing a file or directory using the Mustache template and save it in the destination directory
     *
     * @param component       of the project to be created
     * @param destinationPath the destination path to create the file or directory
     * @param currentPath     the current path of the file or directory to be processed
     * @param relativePath    the relative path to be created in the destination path
     */
    private void process(ComponentAddRequest component, Path destinationPath, Path currentPath, String relativePath) {
        if (currentPath.toFile().isDirectory() && !relativePath.isEmpty()) {
            log.debug("Create directory {}", relativePath);
            if (!Paths.get(destinationPath + relativePath).toFile().mkdirs()) {
                log.error("Could not create directory: {}", destinationPath);
            }
        } else if (currentPath.toFile().isFile()) {

            Template template = mustache.compile(relativePath);
            String renamedFile = template.execute(component);
            File destinationFile = new File(destinationPath + renamedFile);

            // Candidate to be in a future manifest
            if (currentPath.toFile().toString().endsWith(".jar")) {
                log.debug("Copy file {}", relativePath);
                try {
                    FileUtils.copyFile(currentPath.toFile(), destinationFile);
                    return;
                } catch (IOException e) {
                    log.error("Could not copy file {}", currentPath, e);
                }
            }

            log.debug("Processing template path {}", currentPath);
            try (Reader reader = new FileReader(currentPath.toFile());
                 Writer fileWriter = new FileWriter(destinationFile)) {
                log.debug("Current path is a file");
                template = mustache.compile(reader);
                template.execute(component, fileWriter);
            } catch (FileNotFoundException e) {
                log.error("Reader could not find the template path", e);
            } catch (IOException e) {
                log.error("Something went wrong with the Reader/Writer", e);
            }
        } else {
            log.warn("Unsupported file type {}", destinationPath);
        }
    }

    /**
     * Retrieves the relative path from the complete template path
     *
     * @param component               the template variables
     * @param sourceTemplateDirectory the URI of the source template directory
     * @param path                    the path to process
     * @return Path of the relative directory
     */
    private String getRelativePath(ComponentAddRequest component, URI sourceTemplateDirectory, Path path) {
        Path sourcePath = Paths.get(sourceTemplateDirectory);
        String relativePath = StringUtils.removeStart(path.toString(), sourcePath.toString());

        String filename = null;
        if (path.toFile().isFile()) {
            log.debug("Path is a file, removing the filename before converting the path");
            Path currentPath = Paths.get(relativePath);
            relativePath = currentPath.getParent().toString();
            filename = currentPath.getFileName().toString();
        }

        // Candidate to be in a future manifest
        Template template = mustache.compile(relativePath);
        relativePath = template.execute(component).replaceAll("\\.", Matcher.quoteReplacement(File.separator));

        if (filename != null) {
            relativePath += File.separatorChar + filename;
        }

        return relativePath;
    }

    /**
     * Create a temporary directory and a sub directory in it with the provided name
     *
     * @param name of the sub directory to create in the temporary directory
     * @return Path of the temporary directory created
     * @throws IOException if cannot create the temporary directory
     */
    private Path createDestinationDirectory(String name) throws IOException {
        Path temporaryPath = createTempDirectory(null);

        return createDirectories(Paths.get(temporaryPath.toString(), name));
    }

    /**
     * Find the path of the template based on an application type
     *
     * @param applicationType {@link ApplicationType} of the project to be created
     * @return URI of the template path
     * @throws URISyntaxException       if the path is not a valid URI
     * @throws NullPointerException     if the path is null
     * @throws InvalidTemplateException if the {@link ApplicationType} is invalid
     */
    private URI getTemplatePath(ApplicationType applicationType) throws URISyntaxException, InvalidTemplateException {
        log.debug("Get the '{}' template directory path", applicationType);

        String templatePath = sourceTemplatePath;
        switch (applicationType) {
            case JAVA_SPRING_BOOT:
                templatePath += "springboot";
                break;
            case JAVA_SPRING_BOOT_2:
                templatePath += "springboot2";
                break;
            case JAVA_LIBRARY:
                templatePath += "springboot";
                break;
            case NODE:
                templatePath += "node";
                break;
            case REACT:
                templatePath += "react";
                break;
            default:
                log.error("Could not find a valid template path for Application Type {}", applicationType);
                throw new InvalidTemplateException("Could not find a valid template for this application type {}" + applicationType);
        }
        log.trace("Found template path {}", templatePath);

        log.debug("Get the template directory path");
        ClassLoader classLoader = getClass().getClassLoader();
        URL templateResource = classLoader.getResource(templatePath);
        return Objects.requireNonNull(templateResource).toURI();
    }
}
