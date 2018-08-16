/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.google.common.annotations.VisibleForTesting;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component("react_npm")
@Slf4j
@React
class NpmIgnoreCreator implements FileCreator<ProjectCreation> {
    @VisibleForTesting
    static final String NPM_IGNORE_TEMPLATE_PATH = "templates/projectCreation/react/npmignore.tpl";
    private final FileProcessor fileProcessor;

    public NpmIgnoreCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        log.info("Creating .npmignore file");
        InputStream inputStream = this.fileProcessor.loadResourceFromClassPath(NPM_IGNORE_TEMPLATE_PATH);
        File file = this.fileProcessor.touch(Paths.get(request.getRootDir(), ".npmignore"));
        this.fileProcessor.copy(inputStream, file);
    }

}