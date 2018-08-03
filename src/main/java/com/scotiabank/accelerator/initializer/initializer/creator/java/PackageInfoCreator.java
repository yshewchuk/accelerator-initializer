/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.java;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.initializer.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.initializer.creator.CreatorConstants;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@JavaLibrary
class PackageInfoCreator implements FileCreator<ProjectCreation> {

    static final String PACKAGE_INFO_TPL_PATH = "projectCreation/package-info.tpl";
    private final FileProcessor fileProcessor;

    public PackageInfoCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        log.info("Creating package-info.java file");
        Path srcPath = Paths.get(request.getRootDir(), CreatorConstants.SRC_TEST_JAVA_PATH);
        Path packagePath = Paths.get("com", request.getProjectKey().toLowerCase(), "package-info.java");
        new PackageInfo(fileProcessor)
            .create(srcPath.resolve(packagePath), request.resolvePackageName());
    }
    
    @Override
    public int order() {
        return FileCreationOrder.PACKAGE_INFO_TEST.order();
    }
}