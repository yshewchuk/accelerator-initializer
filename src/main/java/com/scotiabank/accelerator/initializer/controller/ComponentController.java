/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.controller;

import com.scotiabank.accelerator.initializer.controller.request.ProjectProperties;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.ProjectCreationService;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.engine.InvalidTemplateException;
import com.scotiabank.accelerator.initializer.engine.TemplateProcessor;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@Slf4j
public class ComponentController {

    private static final String TEMP_PATH = "%s/%s";

    private final ProjectCreationService projectCreationService;
    private final FileProcessor fileProcessor;
    private final String rootDir;
    private final TemplateProcessor templateProcessor;

    public ComponentController(ProjectCreationService projectCreationService,
                               FileProcessor fileProcessor,
                               String rootDir,
                               TemplateProcessor templateProcessor) {
        this.projectCreationService = checkNotNull(projectCreationService);
        this.fileProcessor = checkNotNull(fileProcessor);
        this.rootDir = checkNotNull(rootDir);
        this.templateProcessor = templateProcessor;
    }

    @PostMapping("/api/project/generate")
    public ResponseEntity<byte[]> userDownload(@Validated @RequestBody ProjectProperties component) throws InvalidTemplateException, IOException, URISyntaxException {
        byte[] content;
        if (component.getType().equals(ApplicationType.JAVA_SPRING_BOOT)) {
            // Only the Spring Boot 1.5.x template will be using the new engine until we migrate the other templates
            content = templateProcessor.createApplication(component);
        } else {
            content = projectCreationService.create(convertToProjectCreation(component));
        }

        String contentDispositionValue = "attachment; filename=\"" + component.getName() + ".zip\"";
        return ResponseEntity.ok()
                .header("Content-Type", "application/zip")
                .header("Content-Disposition", contentDispositionValue)
                .body(content);
    }

    private ProjectCreation convertToProjectCreation(ProjectProperties request) {
        return  ProjectCreation.builder()
                .group(request.getGroup())
                .name(request.getName())
                .type(request.getType())
                .rootDir(initProjectDir(request))
                .build();
    }

    private String initProjectDir(ProjectProperties request) {
        String projectName = String.format(TEMP_PATH, request.getGroup().toLowerCase(), request.getName().toLowerCase());
        File path = Paths.get(rootDir, projectName).toFile();
        fileProcessor.createDirectories(path);
        log.info("Creating path {} ", path);
        return path.getAbsolutePath();
    }
}