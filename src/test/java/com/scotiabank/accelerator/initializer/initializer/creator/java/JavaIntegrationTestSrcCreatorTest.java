/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.java;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;

public class JavaIntegrationTestSrcCreatorTest {
    
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<File> fileCaptor;
    private FileCreator<ProjectCreation> srcFileCreator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.srcFileCreator = new JavaIntegrationTestSrcCreator(fileProcessor);
    }

    @Test
    public void assertOrderIsBiggerThanSrcCreator() {
        assertTrue(srcFileCreator.order() > FileCreationOrder.SRC_FOLDER.order());
    }

    @Test
    public void assertFolderIsCreated() throws IOException {
        ProjectCreation request = ProjectCreation.builder()
                .rootDir(folder.getRoot().getAbsolutePath())
                .build();
        srcFileCreator.create(request);
        verify(fileProcessor, times(2)).createDirectories(this.fileCaptor.capture());
        assertTrue(this.fileCaptor
            .getAllValues()
            .stream()
            .map(File::getPath)
            .allMatch(this::srcIsCreatedUnderRootFolder));
    }
    
    private boolean srcIsCreatedUnderRootFolder(String input) {
        return input.equals(folder.getRoot().getPath()+"/src/acceptanceTest/java")
               || input.equals(folder.getRoot().getPath()+"/src/acceptanceTest/resources");
    }
}