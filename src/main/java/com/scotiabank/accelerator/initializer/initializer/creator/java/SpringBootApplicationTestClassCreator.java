/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.java;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.scotiabank.accelerator.initializer.initializer.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.CreatorConstants;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SpringBoot
class SpringBootApplicationTestClassCreator implements FileCreator<ProjectCreation> {
    
    static final String APPLICATION_TEST_TPL_PATH = "projectCreation/ApplicationTest.tpl";
    private final FileProcessor fileProcessor;

    public SpringBootApplicationTestClassCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        log.info("Creating SpringBoot ApplicationTest.java file");
        Path srcPath = Paths.get(request.getRootDir(), CreatorConstants.SRC_TEST_JAVA_PATH);
        Path packagePath = Paths.get("com", request.getProjectKey().toLowerCase(), "ApplicationTest.java");
        File applicationJavaClass = fileProcessor.touch(srcPath.resolve(packagePath));
        writeContentTo(applicationJavaClass, request);
    }
    
    private void writeContentTo(File applicationJavaClass, ProjectCreation request) {
        String packageValue = request.resolvePackageName();
        String content = fileProcessor.processTemplate(APPLICATION_TEST_TPL_PATH, ImmutableMap.of("PACKAGE", packageValue));
        fileProcessor.writeContentTo(applicationJavaClass, content);
    }
    
    @Override
    public int order() {
        return FileCreationOrder.APPLICATION_TEST_CLASS.order();
    }
}