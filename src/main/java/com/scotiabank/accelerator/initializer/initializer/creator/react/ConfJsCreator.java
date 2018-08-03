/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.react;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.scotiabank.accelerator.initializer.initializer.creator.annotation.React;
import com.scotiabank.accelerator.initializer.initializer.creator.common.IntegrationTestFolderCreator;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component("react_conf")
@Slf4j
@React
class ConfJsCreator implements FileCreator<ProjectCreation> {

    static final String CONF_JS_TPL_PATH = "templates/projectCreation/react/conf.js.tpl";
    private final FileProcessor fileProcessor;

    public ConfJsCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating conf.js file");
        InputStream inputStream = this.fileProcessor.loadResourceFromClassPath(CONF_JS_TPL_PATH);
        Path integrationTestPath = Paths.get(request.getRootDir(), IntegrationTestFolderCreator.INTEGRATION_TEST_PATH);
        Path confJsPath = Paths.get("conf.js");
        File confJs = fileProcessor.touch(integrationTestPath.resolve(confJsPath));
        this.fileProcessor.copy(inputStream, confJs);
    }
    
    @Override
    public int order() {
        return FileCreationOrder.CONF_JS.order();
    }
    
}