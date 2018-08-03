/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.impl;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.ProjectCreationService;
import com.scotiabank.accelerator.initializer.initializer.creator.ProjectCreator;
import com.scotiabank.accelerator.initializer.initializer.event.InitializerCleanUpEvent;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.initializer.zip.ZipFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Slf4j
class ProjectCreationServiceImpl implements ProjectCreationService {


    private final List<ProjectCreator<ProjectCreation>> projectCreators;
    private final ApplicationEventPublisher publisher;
    private final FileProcessor fileProcessor;
    private final ZipFile zipFile;

    public ProjectCreationServiceImpl(List<ProjectCreator<ProjectCreation>> projectCreators,
                                      ApplicationEventPublisher publisher,
                                      FileProcessor fileProcessor,
                                      ZipFile zipFile) {
        this.zipFile = checkNotNull(zipFile);
        this.fileProcessor = checkNotNull(fileProcessor);
        this.publisher = checkNotNull(publisher);
        this.projectCreators = checkNotNull(projectCreators);
    }

    @Override
    public byte[] create(ProjectCreation projectCreation) {
        this.projectCreators
            .stream()
            .filter(creator -> !creator.skip(projectCreation))
            .forEach(creator -> creator.create(projectCreation));

        byte[] content = createZipAndConvertToByteArray(projectCreation.getRootDir());
        dispatchCleanUpEvent(projectCreation);
        return content;
    }

    private void dispatchCleanUpEvent(ProjectCreation projectCreation) {
        publisher.publishEvent(new InitializerCleanUpEvent(projectCreation.getRootDir()));
    }

    private byte[] createZipAndConvertToByteArray(String rootDir) {
        File zip = this.zipFile.zip(rootDir);
        return this.fileProcessor.fileToByteArray(zip);

    }
}