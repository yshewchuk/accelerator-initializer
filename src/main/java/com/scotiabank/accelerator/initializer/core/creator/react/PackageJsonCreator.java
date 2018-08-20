/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component("react_package")
@Slf4j
@React
class PackageJsonCreator implements FileCreator<ProjectCreation> {

    static final String PACKAGE_JSON_TPL_PATH = "projectCreation/react/package.json.tpl";
    private final FileProcessor fileProcessor;

    public PackageJsonCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating package.json file");
        Path rootPath = Paths.get(request.getRootDir());
        Path packageJsonPath = Paths.get("package.json");
        File packageJson = fileProcessor.touch(rootPath.resolve(packageJsonPath));
        writeContentTo(packageJson, request);
    }
    
    private void writeContentTo(File packageJson, ProjectCreation request) {
        String projectNameValue = request.getGroup().toLowerCase();
        String repoNameValue = request.getName().toLowerCase();
        String appNameValue = projectNameValue + "-" + repoNameValue;
        String content = fileProcessor.processTemplate(PACKAGE_JSON_TPL_PATH, ImmutableMap.of("APP_NAME", appNameValue, "REPO_NAME", repoNameValue, "PROJECT_NAME", projectNameValue));
        fileProcessor.writeContentTo(packageJson, content);
    }

}