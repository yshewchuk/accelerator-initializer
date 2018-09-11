/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.impl;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.ProjectCreationService;
import com.scotiabank.accelerator.initializer.core.creator.ProjectCreator;
import com.scotiabank.accelerator.initializer.core.event.InitializerCleanUpEvent;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.core.zip.ZipFile;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProjectCreationServiceTest {
    
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
        this.creator = new ProjectCreationServiceImpl(asList(projectCreator2, projectCreator1), publisher,fileProcessor,zipFile);
    }

    @Test
    public void assertPublishEvent() {
        ProjectCreation creation = ProjectCreation.builder()
            .group("hopper")
            .name("initializer")
            .type(ApplicationType.NODE).build();

        this.creator.create(creation);
        verify(this.publisher, times(1)).publishEvent(any(InitializerCleanUpEvent.class));
    }
}