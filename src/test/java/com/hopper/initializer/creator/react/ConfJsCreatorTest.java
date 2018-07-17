/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.react;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreationOrder;
import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.model.ProjectCreation;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfJsCreatorTest {

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
        this.creator = new ConfJsCreator(fileProcessor);
    }
    
    @Test
    public void assertOrder() {
        assertTrue(creator.order() == FileCreationOrder.CONF_JS.order());
        assertTrue(creator.order() > FileCreationOrder.INTEGRATIONTEST_FOLDER.order());
    }
    
    @Test
    public void assertConfJsIsCreated() {
        ProjectCreation request = ProjectCreation.builder()
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).touch(pathCaptor.capture());
        Path confJsPath = pathCaptor.getValue();
        assertEquals("./acceptanceTest/conf.js", confJsPath.toString());
    }

    @Test
    public void assertItCopiesContentToFile() throws IOException {
        File f = folder.newFile("conf.js");
        when(this.fileProcessor.loadResourceFromClassPath(anyString())).thenReturn(new FileInputStream(f));
        when(this.fileProcessor.touch(any())).thenReturn(f);
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).copy(any(), eq(f));
    }
}