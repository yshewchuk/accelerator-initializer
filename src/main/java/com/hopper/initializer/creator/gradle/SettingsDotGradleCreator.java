/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer.creator.gradle;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.hopper.initializer.creator.annotation.JavaLibrary;
import com.hopper.initializer.creator.annotation.SpringBoot;
import com.hopper.initializer.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.hopper.initializer.FileProcessor;
import com.hopper.initializer.creator.FileCreator;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@SpringBoot
@JavaLibrary
class SettingsDotGradleCreator implements FileCreator<ProjectCreation> {

    private static final String PROJECT_NAME = "rootProject.name = '%s'";
    private final FileProcessor fileProcessor;

    public SettingsDotGradleCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }

    @Override
    public void create(ProjectCreation request) {
        Path settingGradlePath = Paths.get(request.getRootDir(),"settings.gradle");
        File settingsDotGralde = this.fileProcessor.touch(settingGradlePath);
        String projectNameProperty = String.format(PROJECT_NAME, request.getRepositoryName());
        this.fileProcessor.writeContentTo(settingsDotGralde, projectNameProperty);
    }
    
    
}