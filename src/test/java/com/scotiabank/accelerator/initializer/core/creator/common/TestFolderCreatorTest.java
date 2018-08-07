/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.common;

import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class TestFolderCreatorTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private FileCreator<ProjectCreation> creator;

    
    @Before
    public void before() {
        this.creator = new TestFolderCreator();
    }
    
    @Test
    public void assertFolderIsCreated() throws IOException {
        ProjectCreation request = ProjectCreation.builder()
                        .rootDir(folder.getRoot().getAbsolutePath())
                        .build();
        creator.create(request);
        assertTrue(Paths.get(request.getRootDir(), "test").toFile().exists());
    }
}