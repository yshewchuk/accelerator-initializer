/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.CreatorConstants;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SpringBoot
@JavaLibrary
class JavaPackageCreator implements FileCreator<ProjectCreation> {

    private final FileProcessor fileProcessor;
    
    public JavaPackageCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        log.info("Creating main, test srcs and packages");
        String packagePath = String.format(CreatorConstants.PACKAGE_PATH, request.getGroup().toLowerCase());
        Path packages = Paths.get(request.getRootDir(), CreatorConstants.SRC_MAIN_JAVA_PATH, packagePath);
        Path testSrc = Paths.get(request.getRootDir(),  CreatorConstants.SRC_TEST_JAVA_PATH, packagePath);
        fileProcessor.createDirectories(packages.toFile());
        fileProcessor.createDirectories(testSrc.toFile());
    }

    @Override
    public int order() {
        return FileCreationOrder.JAVA_PACKAGES.order();
    }
    
}