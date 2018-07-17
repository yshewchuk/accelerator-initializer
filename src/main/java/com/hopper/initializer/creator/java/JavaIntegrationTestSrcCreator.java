/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.java;

import java.nio.file.Paths;

import com.hopper.initializer.creator.annotation.SpringBoot;
import com.hopper.initializer.creator.FileCreationOrder;
import com.hopper.initializer.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreator;

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