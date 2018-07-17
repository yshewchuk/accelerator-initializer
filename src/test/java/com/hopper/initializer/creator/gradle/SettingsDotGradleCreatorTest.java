/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.gradle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import com.hopper.initializer.model.ProjectCreation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreator;

public class SettingsDotGradleCreatorTest {
    @Mock
    private FileProcessor fileProcessor;
    
    private FileCreator<ProjectCreation> creator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new SettingsDotGradleCreator(fileProcessor);
    }

    @Test
    public void assertItCreateSettingsDotGradle() {
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).touch(Paths.get("./settings.gradle"));
    }
    
    @Test
    public void assertItCopiesContentToFile() throws FileNotFoundException {
        File f = new File("./settings.gradle");
        when(this.fileProcessor.touch(any())).thenReturn(f);
        ProjectCreation request = ProjectCreation.builder().repositoryName("hopper").rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).writeContentTo(eq(f), eq("rootProject.name = 'hopper'"));
    }
}