package com.hopper.engine;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.scotiabank.accelerator.initializer.controller.request.ComponentAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;

@Component
@Slf4j
public class TemplateProcessor {

    private final Mustache.Compiler mustache;
    private final String sourceTemplatePath;

    public TemplateProcessor(Mustache.Compiler mustache,
                             @Value("${initializer.template-path}") String sourceTemplatePath) {
        this.mustache = mustache;
        this.sourceTemplatePath = sourceTemplatePath;
    }

    public File createApplication(ComponentAddRequest component) throws InvalidTemplateException, URISyntaxException {
        log.debug("Start the creation of application '{}' with template '{}'", component.getName(), component.getType());

        log.debug("Get the source directory URL of the template");
        URI sourceTemplateDirectory = getTemplatePath(component.getType());
        String parentPath = new File(sourceTemplateDirectory).getPath();
        log.trace("Source template directory is {}", sourceTemplateDirectory.getPath());

        log.debug("Traverse directory and process each template files");
        try (Stream<Path> paths = walk(Paths.get(sourceTemplateDirectory))) {
            paths
                    .forEach(path -> {
                        log.trace("Current path: {}", path);

                        log.debug("Generating destination path");
                        String destinationPath = StringUtils.removeStart(path.toString(), parentPath);
                        destinationPath += component.getName();

                        // For a simple file system with Unix-style paths and behavior:
//                        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
//                        Path foo = fs.getPath("/foo");
//                        Files.createDirectory(foo);

                        log.info("Path {}", destinationPath);

//                        if (Files.isDirectory(path)) {
//                            log.info("folder");
//                        }
//                        // on folder
//                        processFile(path);
//                        log.info("{}", path);
                    });
//                    .forEach(this::processFile);
        } catch (IOException e) {
            log.error("Something went wrong while traversing the source template directory", e);
        }

        return null;
    }

    private void processFile(Path currentPath2, ComponentAddRequest component) {
        File currentPath = currentPath2.toFile();
        log.debug("Processing template path {}", currentPath.getAbsolutePath());
        try (Reader reader = new FileReader(currentPath);
             Writer fileWriter = new FileWriter(currentPath + "test")) {

            if (Files.isDirectory(currentPath2)) {
                log.debug("Current path is a directory");

                String patternMustache = "(.+?)(\\{\\{)(.+?)(}})(.+?)";
                Pattern pattern = Pattern.compile(patternMustache);
                Matcher matcher = pattern.matcher(currentPath.getName());

                if (matcher.matches()) {
                    log.debug("Found a mustache in directory name");
//                    currentPath.renameTo(new File(""));
//                    String newname = matcher.replaceAll(templateVariables.get("GROUP_ID"));
//                    log.debug("Directory renamed to {}", newname);
                }
            } else {
                // TODO Also rename if file

                log.debug("Current path is a file");
                Template template = mustache.compile(reader);
                template.execute(component, fileWriter);
            }
        } catch (FileNotFoundException e) {
            log.error("Reader could not find the template path", e);
        } catch (IOException e) {
            log.error("Something went wrong with the Reader/Writer", e);
        }
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
