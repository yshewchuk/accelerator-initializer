/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.creator.common.TestFolderCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
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
class AppTestJsCreator implements FileCreator<ProjectCreation> {

    private static final String APP_TEST_JS_TPL_PATH = "templates/projectCreation/react/App.test.js.tpl";
    private final FileProcessor fileProcessor;

    public AppTestJsCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating App.test.js file");
        InputStream inputStream = this.fileProcessor.loadResourceFromClassPath(APP_TEST_JS_TPL_PATH);
        Path appPath = Paths.get(request.getRootDir(), TestFolderCreator.TEST_PATH);
        Path appTestJsPath = Paths.get("App.test.js");
        File appTestJs = fileProcessor.touch(appPath.resolve(appTestJsPath));
        this.fileProcessor.copy(inputStream, appTestJs);
    }
    
    @Override
    public int order() {
        return FileCreationOrder.APP_TEST_JS.order();
    }
}