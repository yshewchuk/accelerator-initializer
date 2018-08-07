/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import com.scotiabank.accelerator.initializer.core.zip.ZipFile;
import com.scotiabank.accelerator.initializer.core.zip.ZipFileImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ZipFileTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private ZipFile zipFile;
    
    @Before
    public void before() {
        this.zipFile = new ZipFileImpl();
    }
    
    @Test
    public void assertItCreatesAZipFile() throws IOException {
        File file = folder.newFolder("test");
        this.zipFile.zip(file.getAbsolutePath());
        File zip = new File(file.getParentFile(), "test.zip");
        assertTrue(zip.exists());
    }
}