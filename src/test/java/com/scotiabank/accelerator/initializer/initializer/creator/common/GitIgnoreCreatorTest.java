/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.common;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GitIgnoreCreatorTest {
    
    @Mock
    private FileProcessor fileProcessor;
    @Captor
    private ArgumentCaptor<Map<String,Object>> mapCaptor;

    private FileCreator<ProjectCreation> creator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new GitIgnoreCreator(fileProcessor);
    }

    @Test
    public void assertItCreateFile() {
        ProjectCreation request = ProjectCreation.builder().rootDir(".").build();
        creator.create(request);
        verify(this.fileProcessor, times(1)).touch(Paths.get("./.gitignore"));
    }

    @Test
    public void assertTemplateIsParsed() {
        ProjectCreation request = ProjectCreation.builder()
                .rootDir(".")
                .type(ApplicationType.NODE)
                .build();
        this.creator.create(request);
        verify(this.fileProcessor, times(1)).processTemplate(eq(GitIgnoreCreator.GIT_IGNORE_TEMPLATE_PATH),
                mapCaptor.capture());

        Map<String, Object> args = mapCaptor.getValue();
        assertTrue((boolean) args.get("IS_NODE"));
        assertFalse((boolean) args.get("IS_JAVA_BASED"));
        assertFalse((boolean) args.get("IS_REACT"));
    }
}