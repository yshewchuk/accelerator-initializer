/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.node;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.scotiabank.accelerator.initializer.initializer.creator.annotation.Node;
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
@Node
class HealthCheckJsCreator implements FileCreator<ProjectCreation> {

    static final String HEALTHCHECK_JS_TPL_PATH = "templates/projectCreation/node/healthCheck.js.tpl";
    private final FileProcessor fileProcessor;

    public HealthCheckJsCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating healthCheck.js file");
        InputStream inputStream = this.fileProcessor.loadResourceFromClassPath(HEALTHCHECK_JS_TPL_PATH);
        Path integrationTestPath = Paths.get(request.getRootDir(), IntegrationTestFolderCreator.INTEGRATION_TEST_PATH);
        Path healthCheckJsPath = Paths.get("healthCheck.js");
        File healthCheckJs = fileProcessor.touch(integrationTestPath.resolve(healthCheckJsPath));
        this.fileProcessor.copy(inputStream, healthCheckJs);
    }
    
    @Override
    public int order() {
        return FileCreationOrder.HEALTHCHECK_JS.order();
    }
    
}