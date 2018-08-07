/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.React;
import com.scotiabank.accelerator.initializer.core.creator.common.SrcFolderCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Slf4j
@React
class IndexHtmlCreator implements FileCreator<ProjectCreation> {

    static final String INDEX_HTML_TPL_PATH = "projectCreation/react/index.html.tpl";
    private final FileProcessor fileProcessor;

    public IndexHtmlCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating index.html file");
        Path srcPath = Paths.get(request.getRootDir(), SrcFolderCreator.SRC_PATH);
        Path indexHtmlPath = Paths.get("index.html");
        File packageJson = fileProcessor.touch(srcPath.resolve(indexHtmlPath));
        writeContentTo(packageJson, request);
    }
    
    private void writeContentTo(File packageJson, ProjectCreation request) {
        String repoNameValue = request.getRepositoryName().toLowerCase();
        String content = fileProcessor.processTemplate(INDEX_HTML_TPL_PATH, ImmutableMap.of("REPO_NAME", repoNameValue));
        fileProcessor.writeContentTo(packageJson, content);
    }

}