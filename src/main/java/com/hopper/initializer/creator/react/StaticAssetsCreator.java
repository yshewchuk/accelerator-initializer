/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.react;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.creator.annotation.React;
import com.hopper.initializer.model.ProjectCreation;
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
public class StaticAssetsCreator implements FileCreator<ProjectCreation> {

    static final String INDEX_CSS_TPL_PATH = "templates/projectCreation/react/index.css.tpl";
    static final String APP_CSS_TPL_PATH = "templates/projectCreation/react/App.css.tpl";
    static final String LOGO_SVG_PATH = "templates/projectCreation/react/logo.svg";
    static final String FAVICON_ICO_PATH = "templates/projectCreation/react/favicon.ico";
    private final FileProcessor fileProcessor;

    public StaticAssetsCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        log.info("Creating static assets for project type {}", request.getType());
        Path cssFolder = Paths.get(request.getRootDir(), "static", "css");
        cssFolder.toFile().mkdirs();
        Path imagesFolder = Paths.get(request.getRootDir(), "static", "images");
        imagesFolder.toFile().mkdirs();
        InputStream isIndexCss = this.fileProcessor.loadResourceFromClassPath(INDEX_CSS_TPL_PATH);
        File indexCss = this.fileProcessor.touch(cssFolder.resolve("index.css"));
        this.fileProcessor.copy(isIndexCss, indexCss);

        InputStream isAppCss = this.fileProcessor.loadResourceFromClassPath(APP_CSS_TPL_PATH);
        File appCss = this.fileProcessor.touch(cssFolder.resolve("App.css"));
        this.fileProcessor.copy(isAppCss, appCss);

        InputStream isLogo = this.fileProcessor.loadResourceFromClassPath(LOGO_SVG_PATH);
        File logo = this.fileProcessor.touch(imagesFolder.resolve("logo.svg"));
        this.fileProcessor.copy(isLogo, logo);

        InputStream isFavicon = this.fileProcessor.loadResourceFromClassPath(FAVICON_ICO_PATH);
        File favicon = this.fileProcessor.touch(imagesFolder.resolve("favicon.ico"));
        this.fileProcessor.copy(isFavicon, favicon);
    }
}