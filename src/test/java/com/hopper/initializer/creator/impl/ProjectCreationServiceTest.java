/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hopper.initializer.creator.ProjectCreator;
import com.hopper.initializer.model.ProjectCreation;
import com.hopper.initializer.zip.ZipFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.ProjectCreationService;
import com.hopper.initializer.event.InitializerCleanUpEvent;
import com.hopper.model.ApplicationType;
import com.google.common.collect.Lists;

public class ProjectCreationServiceTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private FileProcessor fileProcessor;
    @Mock
    private ZipFile zipFile;
    @Mock
    private ProjectCreator<ProjectCreation> projectCreator1;
    @Mock
    private ProjectCreator<ProjectCreation> projectCreator2;
    
    private ProjectCreationService creator;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.creator = new ProjectCreationServiceImpl(Lists.newArrayList(projectCreator2, projectCreator1), publisher,fileProcessor,zipFile);
    }

    @Test
    public void assertPublishEvent() {
        ProjectCreation creation = ProjectCreation.builder()
            .projectKey("hopper")
            .repositoryName("initializer")
            .type(ApplicationType.NODE).build();

        this.creator.create(creation);
        verify(this.publisher, times(1)).publishEvent(any(InitializerCleanUpEvent.class));
    }
}