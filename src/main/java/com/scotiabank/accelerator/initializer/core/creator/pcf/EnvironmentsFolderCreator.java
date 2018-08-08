/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.pcf;

import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.core.creator.annotation.Node;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.core.creator.FileCreator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SpringBoot
@Node
@React
class EnvironmentsFolderCreator implements FileCreator<ProjectCreation> {

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating environments folder for project type {}", request.getType());
        Paths.get(request.getRootDir(), "environments")
             .toFile()
             .mkdir();
    }
}