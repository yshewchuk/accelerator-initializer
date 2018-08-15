/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.scotiabank.accelerator.initializer.core.FileProcessor;

public class PackageInfoCreatorTest {
    
    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<Path> pathCaptor;
    
    private FileCreator<ProjectCreation> creator;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new PackageInfoCreator(fileProcessor);
    }
    
    @Test
    public void assertOrder() {
        assertTrue(creator.order() == FileCreationOrder.PACKAGE_INFO_TEST.order());
        assertTrue(creator.order() > FileCreationOrder.JAVA_PACKAGES.order());
    }
    
    @Test
    public void assertPackageInfoIsCreated() {
        ProjectCreation request = ProjectCreation.builder()
                        .projectKey("hopper")
                        .rootDir(".")
                        .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).touch(pathCaptor.capture());
        Path path = pathCaptor.getValue();
        assertEquals(Paths.get("./src/test/java/com/hopper/package-info.java"), path);
    }
    
}