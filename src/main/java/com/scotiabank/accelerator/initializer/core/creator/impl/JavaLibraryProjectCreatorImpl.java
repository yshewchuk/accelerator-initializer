/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.impl;

import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.ProjectCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
class JavaLibraryProjectCreatorImpl implements ProjectCreator<ProjectCreation> {

    private List<FileCreator<ProjectCreation>> fileCreators;

    public JavaLibraryProjectCreatorImpl(@JavaLibrary List<FileCreator<ProjectCreation>> fileCreators) {
        this.fileCreators = checkNotNull(fileCreators);
    }

    @Override
    public boolean skip(ProjectCreation projectCreation) {
        return ApplicationType.JAVA_LIBRARY != projectCreation.getType();
    }

    @Override
    public void create(ProjectCreation request) {
        fileCreators
                .stream()
                .sorted(Comparator.comparingInt(FileCreator::order))
                .forEach(creator -> creator.create(request));
    }
}