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

@Component
@Slf4j
@React
class AppSpecJsCreator implements FileCreator<ProjectCreation> {

    static final String APP_SPEC_JS_TPL_PATH = "templates/projectCreation/react/App_spec.js.tpl";
    private final FileProcessor fileProcessor;

    public AppSpecJsCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating App_spec.js file");
        InputStream inputStream = this.fileProcessor.loadResourceFromClassPath(APP_SPEC_JS_TPL_PATH);
        Path integrationTestPath = Paths.get(request.getRootDir(), IntegrationTestFolderCreator.INTEGRATION_TEST_PATH);
        Path appSpecJsPath = Paths.get("App_spec.js");
        File appSpecJs = fileProcessor.touch(integrationTestPath.resolve(appSpecJsPath));
        this.fileProcessor.copy(inputStream, appSpecJs);
    }
    
    @Override
    public int order() {
        return FileCreationOrder.APP_SPEC_JS.order();
    }
}