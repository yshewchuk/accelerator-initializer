/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.controller;

import com.scotiabank.accelerator.initializer.controller.request.ComponentAddRequest;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.ProjectCreationService;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@Slf4j
public class ComponentController {

    private static final String TEMP_PATH = "%s/%s";

    private final ProjectCreationService projectCreationService;
    private final FileProcessor fileProcessor;
    private final String rootDir;

    public ComponentController(ProjectCreationService projectCreationService, FileProcessor fileProcessor, String rootDir) {
        this.projectCreationService = checkNotNull(projectCreationService);
        this.fileProcessor = checkNotNull(fileProcessor);
        this.rootDir = checkNotNull(rootDir);
    }

    @PostMapping("/api/projects/components/download")
    public ResponseEntity<byte[]> userDownload(@Validated @RequestBody ComponentAddRequest component) {
        byte[] content = projectCreationService.create(convertToProjectCreation(component));

        String contentDispositionValue = "attachment; filename=\"" + component.getName() + ".zip\"";
        return ResponseEntity.ok()
                .header("Content-Type", "application/zip")
                .header("Content-Disposition", contentDispositionValue)
                .body(content);
    }

    private ProjectCreation convertToProjectCreation(ComponentAddRequest request) {
        return  ProjectCreation.builder()
                .projectKey(request.getProjectKey())
                .repositoryName(request.getName())
                .type(request.getType())
                .rootDir(initProjectDir(request))
                .build();
    }

    private String initProjectDir(ComponentAddRequest request) {
        String projectName = String.format(TEMP_PATH, request.getProjectKey().toLowerCase(), request.getName().toLowerCase());
        File path = Paths.get(rootDir, projectName).toFile();
        fileProcessor.createDirectories(path);
        log.info("Creating path {} ", path);
        return path.getAbsolutePath();
    }
}