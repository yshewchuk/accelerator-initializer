/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.common;

import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.core.creator.annotation.Node;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@Slf4j
@SpringBoot
@React
@Node
@JavaLibrary
public class SrcFolderCreator implements FileCreator<ProjectCreation> {

    public static final String SRC_PATH = "src";

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating app folder for project type {}", request.getType());
        Paths.get(request.getRootDir(), SRC_PATH)
             .toFile()
             .mkdir();
    }
    
}