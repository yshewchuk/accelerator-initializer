/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.java;

import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.initializer.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@SpringBoot
class JavaIntegrationTestSrcCreator implements FileCreator<ProjectCreation> {
    
    private static final String SRC_INTEGRATION_TEST_RESOURCES = "src/acceptanceTest/resources";
    private static final String SRC_INTEGRATION_TEST_JAVA = "src/acceptanceTest/java";
    private final FileProcessor processor;

    public JavaIntegrationTestSrcCreator(FileProcessor processor) {
        this.processor = checkNotNull(processor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        processor.createDirectories(Paths.get(request.getRootDir(), SRC_INTEGRATION_TEST_JAVA).toFile());
        processor.createDirectories(Paths.get(request.getRootDir(), SRC_INTEGRATION_TEST_RESOURCES).toFile());
    }
    
    @Override
    public int order() {
        return FileCreationOrder.JAVA_INTEGRATION_TEST_FOLDER.order();
    }
}