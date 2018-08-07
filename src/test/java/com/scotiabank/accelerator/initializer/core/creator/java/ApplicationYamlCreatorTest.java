/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.model.ApplicationType;

public class ApplicationYamlCreatorTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<Path> fileCaptor;
    private FileCreator<ProjectCreation> srcFileCreator;

    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.srcFileCreator = new ApplicationYamlCreator(fileProcessor);
    }
    
    @Test
    public void assertItRunsAfterResourceFolder() {
       assertTrue(this.srcFileCreator.order() > FileCreationOrder.JAVA_MAIN_FOLDER.order());
    }

    @Test
    public void assertFileIsCreated() throws IOException {
        ProjectCreation request = ProjectCreation.builder()
                .rootDir(folder.getRoot().getAbsolutePath())
                .type(ApplicationType.JAVA_SPRING_BOOT)
                .build();
        srcFileCreator.create(request);
        verify(fileProcessor, times(1)).touch(this.fileCaptor.capture());
        Path path = this.fileCaptor.getValue();
        assertEquals(Paths.get(folder.getRoot().getAbsolutePath()+ApplicationYamlCreator.APPLICATION_YML_PATH), path);
    }
    
    
}