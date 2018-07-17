/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.java;

import java.nio.file.Paths;

import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.creator.annotation.SpringBoot;
import com.hopper.initializer.creator.FileCreationOrder;
import com.hopper.initializer.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.hopper.initializer.FileProcessor;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SpringBoot
class ApplicationYamlCreator implements FileCreator<ProjectCreation> {

    static final String APPLICATION_YML_PATH = "/src/main/resources/application.yml";
    private final FileProcessor fileProcessor;

    public ApplicationYamlCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        log.info("Creating application.yml file for {}-{} ", request.getProjectKey(), request.getRepositoryName());
        fileProcessor.touch(Paths.get(request.getRootDir(), APPLICATION_YML_PATH));
    }
    
    @Override
    public int order() {
        return FileCreationOrder.APPLICATION_YML.order();
    }
    
}