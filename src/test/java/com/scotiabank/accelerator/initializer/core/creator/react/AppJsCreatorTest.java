/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppJsCreatorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<Path> pathCaptor;

    private FileCreator<ProjectCreation> creator;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new AppJsCreator(fileProcessor);
    }
    
    @Test
    public void assertOrder() {
        assertTrue(creator.order() == FileCreationOrder.APP_JS.order());
        assertTrue(creator.order() > FileCreationOrder.SRC_FOLDER.order());
    }

    @Test
    public void assertAppJsIsCreated() {
        ProjectCreation request = ProjectCreation.builder()
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).touch(pathCaptor.capture());
        Path healthCheckJsPath = pathCaptor.getValue();
        assertEquals(Paths.get("./src/App.js"), healthCheckJsPath);
    }

    @Test
    public void assertItCopiesContentToFile() throws IOException {
        File f = folder.newFile("App.js");
        when(this.fileProcessor.loadResourceFromClassPath(anyString())).thenReturn(new FileInputStream(f));
        when(this.fileProcessor.touch(any())).thenReturn(f);
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).copy(any(), eq(f));
    }
}