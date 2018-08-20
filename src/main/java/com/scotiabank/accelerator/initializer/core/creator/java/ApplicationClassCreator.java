/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.annotations.VisibleForTesting;
import com.scotiabank.accelerator.initializer.core.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.google.common.collect.ImmutableMap;

import static com.scotiabank.accelerator.initializer.core.creator.CreatorConstants.SRC_MAIN_JAVA_PATH;
import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@JavaLibrary
@SpringBoot
class ApplicationClassCreator implements FileCreator<ProjectCreation> {
    @VisibleForTesting
    static final String APPLICATION_TPL_PATH = "projectCreation/Application.tpl";
    private final FileProcessor fileProcessor;

    public ApplicationClassCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating Application.java file");
        Path srcPath = Paths.get(request.getRootDir(), SRC_MAIN_JAVA_PATH);
        Path packagePath = Paths.get("com", request.getGroup().toLowerCase(), "Application.java");
        File applicationJavaClass = fileProcessor.touch(srcPath.resolve(packagePath));
        writeContentTo(applicationJavaClass, request);
    }
    
    private void writeContentTo(File applicationJavaClass, ProjectCreation request) {
        String packageValue = request.resolvePackageName();
        String content = fileProcessor.processTemplate(APPLICATION_TPL_PATH, ImmutableMap.of("PACKAGE", packageValue, "IS_SPRING_BOOT_APP", isSpringBootApp(request)));
        fileProcessor.writeContentTo(applicationJavaClass, content);
    }

    private boolean isSpringBootApp(ProjectCreation request) {
        return request.isSpringBootApp();
    }

    @Override
    public int order() {
        return FileCreationOrder.APPLICATION_CLASS.order();
    }
}