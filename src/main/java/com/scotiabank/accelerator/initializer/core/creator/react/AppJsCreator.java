/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.creator.common.SrcFolderCreator;
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
class AppJsCreator implements FileCreator<ProjectCreation> {

    static final String APP_JS_TPL_PATH = "templates/projectCreation/react/App.js.tpl";
    private final FileProcessor fileProcessor;

    public AppJsCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating app.js file");
        InputStream inputStream = this.fileProcessor.loadResourceFromClassPath(APP_JS_TPL_PATH);
        Path appPath = Paths.get(request.getRootDir(), SrcFolderCreator.SRC_PATH);
        Path appJsPath = Paths.get("App.js");
        File appJs = fileProcessor.touch(appPath.resolve(appJsPath));
        this.fileProcessor.copy(inputStream, appJs);
    }
    
    @Override
    public int order() {
        return FileCreationOrder.APP_JS.order();
    }
}