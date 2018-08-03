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
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebpackConfigJsCreatorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private FileProcessor fileProcessor;
    
    private FileCreator<ProjectCreation> creator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new WebpackConfigJsCreator(fileProcessor);
    }

    @Test
    public void assertItLoadsWebpackConfigJsTemplate() {
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).loadResourceFromClassPath(WebpackConfigJsCreator.WEBPACK_CONFIG_JS_TEMPLATE_PATH);
    }
    
    @Test
    public void assertItCreateFile() {
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).touch(Paths.get("./webpack.config.js"));
    }
    
    @Test
    public void assertItCopiesContentToFile() throws IOException {
        File f = folder.newFile("webpack.config.js");
        when(this.fileProcessor.loadResourceFromClassPath(anyString())).thenReturn(new FileInputStream(f));
        when(this.fileProcessor.touch(any())).thenReturn(f);
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).copy(any(), eq(f));
    }
}