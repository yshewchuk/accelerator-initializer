/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.react;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IndexHtmlCreatorTest {
    
    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<Path> pathCaptor;
    @Captor
    private ArgumentCaptor<Map<String,Object>> mapCaptor;
    
    private FileCreator<ProjectCreation> creator;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new IndexHtmlCreator(fileProcessor);
    }
    
    @Test
    public void assertIndexHtmlIsCreated() {
        ProjectCreation request = ProjectCreation.builder()
                        .repositoryName("test-app")
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).touch(pathCaptor.capture());
        Path indexHtmlPath = pathCaptor.getValue();
        assertEquals(Paths.get("./src/index.html"), indexHtmlPath);
    }
    
    @Test
    public void assertTemplateIsParsed() {
        ProjectCreation request = ProjectCreation.builder()
                        .repositoryName("test-app")
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).processTemplate(eq(IndexHtmlCreator.INDEX_HTML_TPL_PATH),
                                                                mapCaptor.capture());
        
        Map<String, Object> args = mapCaptor.getValue();
        assertEquals("test-app", args.get("REPO_NAME"));
    }
}