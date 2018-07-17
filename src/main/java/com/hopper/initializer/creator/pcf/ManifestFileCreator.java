/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.pcf;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreationOrder;
import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.creator.annotation.Node;
import com.hopper.initializer.creator.annotation.React;
import com.hopper.initializer.creator.annotation.SpringBoot;
import com.hopper.initializer.model.ProjectCreation;
import com.hopper.model.ApplicationType;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Slf4j
@SpringBoot
@Node
@React
class ManifestFileCreator implements FileCreator<ProjectCreation> {
    static final String MANIFEST_FILE_TEMPLATE = "projectCreation/manifest.tpl";
    private static final String APPLICATION_NAME = "%s";
    private static final String FILE_NAME = "manifest.yml";
    private FileProcessor fileProcessor;

    public ManifestFileCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating manifest files for project {}", request.getRepositoryName());
            String templateContent = processTemplate(request);
            String fileName = String.format(FILE_NAME);
            File manifestFile = createFile(fileName, request.getRootDir());
            fileProcessor.writeContentTo(manifestFile, templateContent);
    }

    private String processTemplate(ProjectCreation request) {
        String applicationName = String.format(APPLICATION_NAME, request.getRepositoryName());
        return fileProcessor.processTemplate(MANIFEST_FILE_TEMPLATE,
                ImmutableMap.of("APPLICATION_NAME", applicationName, "IS_NODE_APP", request.isNodeApp(), "IS_REACT", request.getType() == ApplicationType.REACT));
    }

    private File createFile(String fileName, String projectDir) {
        return fileProcessor.touch(Paths.get(projectDir, "environments", fileName));
    }

    @Override
    public int order() {
        return FileCreationOrder.MANIFEST_FILES.order();
    }
}