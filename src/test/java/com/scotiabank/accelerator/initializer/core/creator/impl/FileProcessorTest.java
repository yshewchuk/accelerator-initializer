/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.exception.InitializerException;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.MustacheRenderer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FileProcessorTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Mock
    private MustacheRenderer renderer;
    private FileProcessor processor;
    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.processor = new FileProcessorImpl(renderer);
    }
    
    @Test
    public void assertProcessTemplateCallsMustacheRenderer() {
        this.processor.processTemplate("abc", null);
        verify(this.renderer, times(1)).process("abc", null);
    }
    
    @Test
    public void assertCopyContentToATargetFile() throws IOException {
        File file = folder.newFile("test.txt");
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        this.processor.copy(inputStream, file);
        assertEquals("test", new String(Files.readAllBytes(file.toPath())));
    }
    
    @Test(expected=InitializerException.class)
    public void assertCopyThrowsMissionControlException() throws IOException {
        File f= folder.newFolder("invalid");
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        this.processor.copy(inputStream, f);
    }
    
    @Test
    public void assertTouchCreateANewFile() throws IOException {
        Path path = Paths.get(folder.getRoot().getAbsolutePath(), "test2.txt");
        this.processor.touch(path);
        assertTrue(path.toFile().exists());
    }
    
    @Test
    public void assertWriteContentToATargetFile() throws IOException {
        File file = folder.newFile("test3.txt");
        this.processor.writeContentTo(file, "writeContent");
        assertEquals("writeContent", new String(Files.readAllBytes(file.toPath())));
    }
    
    @Test(expected=InitializerException.class)
    public void assertWriteContentToATargetFileShouldThrowMissionControlException() throws IOException {
        File file = folder.newFolder("test");
        this.processor.writeContentTo(file, "writeContent");
    }
    
    @Test
    public void assertFileIsConvertedToByteArray() throws IOException {
        File file = folder.newFile("test3.txt");
        this.processor.writeContentTo(file, "to byte array");
        byte [] result = this.processor.fileToByteArray(file);
        assertEquals("to byte array", new String(result));
    }
    
    
    @Test(expected=InitializerException.class)
    public void assertFileIsConvertedToByteArrayShouldThrowMissionControlException() throws IOException {
        File file = folder.newFolder("test");
        this.processor.fileToByteArray(file);
    }
    
    @Test
    public void assertCreateDirectories() throws IOException {
        File file = new File(folder.getRoot().getAbsolutePath(), "test3/test2/");
        this.processor.createDirectories(file);
        assertTrue(file.exists());
    }
    
    @Test
    public void assertDelete() throws IOException {
        File file = folder.newFile("delete-me.txt");
        this.processor.delete(file);
        assertFalse(file.exists());
    }
    
    
}