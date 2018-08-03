/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.pcf;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;

import com.scotiabank.accelerator.initializer.initializer.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;

public class ManifestFileCreatorTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Mock
    private FileProcessor processor;
    @Captor
    private ArgumentCaptor<Path> pathArgument;

    private FileCreator<ProjectCreation> creator;
    private static final String FILE_NAME = "manifest.yml";
    
    
    @Before
    public void before() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.creator = new ManifestFileCreator(processor);
        folder.newFolder("environments");
    }
    
    @Test
    public void assertManifestFileAreCreated() throws IOException {
        ProjectCreation request = ProjectCreation.builder()
                        .rootDir(folder.getRoot().getAbsolutePath())
                        .build();
        creator.create(request);
        verify(this.processor, times(1)).touch(pathArgument.capture());
        assertEquals(FILE_NAME, pathArgument.getValue().getFileName().toString());
    }

    @Test
    public void asssertTemplateProcessorIsCalledForEachSpaceAndRegion() throws IOException {
        ProjectCreation request = ProjectCreation.builder()
                .rootDir(folder.getRoot().getAbsolutePath())
                .build();
        creator.create(request);
        verify(this.processor, times(1)).processTemplate(eq(ManifestFileCreator.MANIFEST_FILE_TEMPLATE), any());
        verify(this.processor, times(1)).writeContentTo(any(), any());
    }
    
    @Test
    public void assertOrderIsCorrect() throws IOException {
        assertEquals(FileCreationOrder.MANIFEST_FILES.order(), this.creator.order());
    }
}