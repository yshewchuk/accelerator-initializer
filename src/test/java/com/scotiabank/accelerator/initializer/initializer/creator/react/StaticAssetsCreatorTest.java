/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.react;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StaticAssetsCreatorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private FileProcessor fileProcessor;
    
    private FileCreator<ProjectCreation> creator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new StaticAssetsCreator(fileProcessor);
    }

    @Test
    public void assertItLoadsTemplates() {
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).loadResourceFromClassPath(StaticAssetsCreator.FAVICON_ICO_PATH);
        verify(this.fileProcessor, times(1)).loadResourceFromClassPath(StaticAssetsCreator.LOGO_SVG_PATH);
        verify(this.fileProcessor, times(1)).loadResourceFromClassPath(StaticAssetsCreator.INDEX_CSS_TPL_PATH);
        verify(this.fileProcessor, times(1)).loadResourceFromClassPath(StaticAssetsCreator.APP_CSS_TPL_PATH);
    }
    
    @Test
    public void assertItCreateFiles() {
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).touch(Paths.get("./static/css/App.css"));
        verify(this.fileProcessor, times(1)).touch(Paths.get("./static/css/index.css"));
        verify(this.fileProcessor, times(1)).touch(Paths.get("./static/images/logo.svg"));
        verify(this.fileProcessor, times(1)).touch(Paths.get("./static/images/favicon.ico"));

    }
    
    @Test
    public void assertItCopiesContentToFile() throws IOException {
        File appCss = folder.newFile("App.css");
        File indexCss = folder.newFile("index.css");
        File faviconIco = folder.newFile("favicon.ico");
        File logoSvg = folder.newFile("logo.svg");
        when(this.fileProcessor.loadResourceFromClassPath(StaticAssetsCreator.APP_CSS_TPL_PATH)).thenReturn(new FileInputStream(appCss));
        when(this.fileProcessor.loadResourceFromClassPath(StaticAssetsCreator.INDEX_CSS_TPL_PATH)).thenReturn(new FileInputStream(indexCss));
        when(this.fileProcessor.loadResourceFromClassPath(StaticAssetsCreator.FAVICON_ICO_PATH)).thenReturn(new FileInputStream(faviconIco));
        when(this.fileProcessor.loadResourceFromClassPath(StaticAssetsCreator.LOGO_SVG_PATH)).thenReturn(new FileInputStream(logoSvg));

        when(this.fileProcessor.touch(Paths.get("./static/css/App.css"))).thenReturn(appCss);
        when(this.fileProcessor.touch(Paths.get("./static/css/index.css"))).thenReturn(indexCss);
        when(this.fileProcessor.touch(Paths.get("./static/images/logo.svg"))).thenReturn(logoSvg);
        when(this.fileProcessor.touch(Paths.get("./static/images/favicon.ico"))).thenReturn(faviconIco);

        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).copy(any(), eq(appCss));
        verify(this.fileProcessor, times(1)).copy(any(), eq(indexCss));
        verify(this.fileProcessor, times(1)).copy(any(), eq(logoSvg));
        verify(this.fileProcessor, times(1)).copy(any(), eq(faviconIco));

    }
}