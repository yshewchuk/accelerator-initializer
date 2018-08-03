/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreationOrder;
import com.hopper.initializer.creator.FileCreator;
import com.hopper.initializer.model.ProjectCreation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class JavaIntegrationTestPackageCreatorTest {
    
    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<File> fileCaptor;
    @Captor
    private ArgumentCaptor<Path> pathCaptor;
    private FileCreator<ProjectCreation> creator;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new JavaIntegrationTestPackageCreator(fileProcessor);
    }
    
    @Test
    public void assertOrder() {
        assertTrue(creator.order() == FileCreationOrder.JAVA_PACKAGES.order());
        assertTrue(creator.order() > FileCreationOrder.SRC_FOLDER.order());
    }
    
    @Test
    public void assertPackagePathIsCreated() {
        ProjectCreation request = ProjectCreation.builder()
                        .projectKey("hopper")
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).createDirectories(fileCaptor.capture());
        
        List<String> collectedPaths = fileCaptor.getAllValues()
                  .stream()
                  .map(File::getPath)
                  .collect(Collectors.toList());
        assertEquals(paths(request.getProjectKey()), collectedPaths);
    }
    
    @Test
    public void assertPackageInfoIsCreated() {
        ProjectCreation request = ProjectCreation.builder()
                        .projectKey("hopper")
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).createDirectories(any());
        verify(this.fileProcessor, times(1)).touch(pathCaptor.capture());
                
        assertEquals("package-info.java", pathCaptor.getValue().toFile().getName());
    }
    
    private List<String> paths(String ciadKey) {
        return Lists.newArrayList(Paths.get("./src/acceptanceTest/java/com/hopper").toString());
    }
}