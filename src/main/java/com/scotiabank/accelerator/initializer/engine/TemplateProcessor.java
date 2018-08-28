package com.scotiabank.accelerator.initializer.engine;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;

@Component
@Slf4j
public class TemplateProcessor {

    private ResourceLoader resourceLoader;
    private final String sourceTemplateParentPath;

    public TemplateProcessor(ResourceLoader resourceLoader,
                             @Value("${initializer.template-path}") String sourceTemplateParentPath) {
        this.resourceLoader = resourceLoader;
        this.sourceTemplateParentPath = sourceTemplateParentPath;
    }

    /**
     * Create a new project based on an existing template, customize the values with Mustache template engine and provided in the input param
     *
     * @param request {@link ProjectCreation} attributes to create a new project
     * @throws IOException if one of the files or directory in template could not be processed
     */
    public void createApplication(ProjectCreation request) {
        final Path sourceTemplate;
        try {
            sourceTemplate = Paths.get(resourceLoader.getResource(sourceTemplateParentPath).getFile().toString(), request.getType().toString());
        } catch (IOException e) {
            log.error("Could not locate the source template directory", e);
            return;
        }

        try (Stream<Path> paths = walk(sourceTemplate)) {
            paths
                    .forEach(currentPath -> {
                        Path relativePath = processRelativePath(sourceTemplate, currentPath, request);
                        process(request, currentPath, relativePath.toString());
                    });
        } catch (IOException e) {
            log.error("Something went wrong while traversing the source template directory", e);
        }
    }

    /**
     * Processing a file or directory using the Mustache template and save it in the destination directory
     *
     * @param request      {@link ProjectCreation} containing the template variables
     * @param currentPath  the current path of the file or directory to be processed
     * @param relativePath the relative path to be created in the destination path
     */
    protected void process(ProjectCreation request, Path currentPath, String relativePath) {
        if (currentPath.toFile().isDirectory()
                && !relativePath.isEmpty()
                && (!Paths.get(request.getRootDir() + relativePath).toFile().mkdirs())) {
            log.error("Could not create directory: {}", request.getRootDir());
        }
        if (currentPath.toFile().isFile()) {
            Template template = Mustache.compiler().compile(relativePath);
            String renamedFile = template.execute(request);
            File destinationFile = new File(request.getRootDir() + renamedFile);

            if (currentPath.toFile().toString().endsWith(".jar")) {
                try {
                    FileUtils.copyFile(currentPath.toFile(), destinationFile);
                } catch (IOException e) {
                    log.error("Could not copy file {}", currentPath, e);
                }
            } else {
                try (Reader reader = new FileReader(currentPath.toFile());
                     Writer fileWriter = new FileWriter(destinationFile)) {
                    template = Mustache.compiler().compile(reader);
                    template.execute(request, fileWriter);
                } catch (FileNotFoundException e) {
                    log.error("Reader could not find the template path", e);
                } catch (IOException e) {
                    log.error("Something went wrong with the Reader/Writer", e);
                }
            }
        }
    }

    /**
     * Retrieves the relative path from the complete template path and convert the Mustache variables
     *
     * @param sourceTemplatePath the path of the template location
     * @param currentPath        the current path to process
     * @param request            {@link ProjectCreation} containing the template variables
     * @return Path of the relative directory/file
     */
    protected Path processRelativePath(Path sourceTemplatePath, Path currentPath, ProjectCreation request) {
        String relativePath = StringUtils.removeStart(currentPath.toString(), sourceTemplatePath.toString());

        String extractedFilename = null;
        if (currentPath.toFile().isFile()) {
            relativePath = Paths.get(relativePath).getParent().toString();
            extractedFilename = currentPath.getFileName().toString();
            Template template = Mustache.compiler().compile(extractedFilename);
            extractedFilename = template.execute(request);
        }

        Template template = Mustache.compiler().compile(relativePath);
        relativePath = request.packageToPath(template.execute(request));

        if (extractedFilename != null) {
            relativePath += (relativePath.length() > 1 ? File.separatorChar : "") + extractedFilename;
        }
        return Paths.get(relativePath);
    }
}