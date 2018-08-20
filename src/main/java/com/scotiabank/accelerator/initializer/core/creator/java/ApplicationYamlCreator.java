/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import java.nio.file.Paths;

import com.google.common.annotations.VisibleForTesting;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.core.FileProcessor;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SpringBoot
class ApplicationYamlCreator implements FileCreator<ProjectCreation> {
    @VisibleForTesting
    static final String APPLICATION_YML_PATH = "/src/main/resources/application.yml";
    private final FileProcessor fileProcessor;

    public ApplicationYamlCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        log.info("Creating application.yml file for {}-{} ", request.getGroup(), request.getName());
        fileProcessor.touch(Paths.get(request.getRootDir(), APPLICATION_YML_PATH));
    }
    
    @Override
    public int order() {
        return FileCreationOrder.APPLICATION_YML.order();
    }
    
}