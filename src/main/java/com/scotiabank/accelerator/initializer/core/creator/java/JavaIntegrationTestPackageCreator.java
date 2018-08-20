/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@SpringBoot
class JavaIntegrationTestPackageCreator implements FileCreator<ProjectCreation> {
    public static final String PACKAGE_PATH = "com/%s";
    public static final String SRC_INTEGRATION_TEST_JAVA_PATH = "src/acceptanceTest/java";
    private final FileProcessor fileProcessor;
    
    public JavaIntegrationTestPackageCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        String packagePath = String.format(PACKAGE_PATH, request.getGroup());
        Path integTest = Paths.get(request.getRootDir(),  SRC_INTEGRATION_TEST_JAVA_PATH, packagePath);
        fileProcessor.createDirectories(integTest.toFile());
        new PackageInfo(fileProcessor)
            .create(integTest.resolve("package-info.java"), request.resolvePackageName());
    }
    
    @Override
    public int order() {
        return FileCreationOrder.JAVA_INTEGRATION_PACKAGE.order();
    }
}