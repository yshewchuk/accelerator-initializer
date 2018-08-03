/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.java;

import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.scotiabank.accelerator.initializer.initializer.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.initializer.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@SpringBoot
@JavaLibrary
class JavaMainSrcCreator implements FileCreator<ProjectCreation> {
    
    private final FileProcessor processor;

    public JavaMainSrcCreator(FileProcessor processor) {
        this.processor = checkNotNull(processor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        processor.createDirectories(Paths.get(request.getRootDir(), "src/main/java").toFile());
        processor.createDirectories(Paths.get(request.getRootDir(), "src/main/resources").toFile());
    }
    
    @Override
    public int order() {
        return FileCreationOrder.JAVA_MAIN_FOLDER.order();
    }
}