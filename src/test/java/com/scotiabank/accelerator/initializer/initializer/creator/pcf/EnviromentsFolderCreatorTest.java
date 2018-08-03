/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.pcf;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EnviromentsFolderCreatorTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private FileCreator<ProjectCreation> creator;

    
    @Before
    public void before() {
        this.creator = new EnvironmentsFolderCreator();
    }
    
    @Test
    public void assertFolderIsCreated() throws IOException {
        ProjectCreation request = ProjectCreation.builder()
                        .rootDir(folder.getRoot().getAbsolutePath())
                        .build();
        creator.create(request);
        assertTrue(Paths.get(request.getRootDir(), "environments").toFile().exists());
    }
}