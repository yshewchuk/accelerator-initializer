/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

import com.scotiabank.accelerator.initializer.exception.InitializerException;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.MustacheRenderer;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class FileProcessorImpl implements FileProcessor {

    
    private final MustacheRenderer mustacheRenderer;
    
    public FileProcessorImpl(MustacheRenderer mustacheRenderer) {
        this.mustacheRenderer = checkNotNull(mustacheRenderer);
    }
    
    @Override
    public String processTemplate(String templateName, Map<String, Object> vars) {
        return mustacheRenderer.process(templateName, vars);
    }

    @Override
    public void writeContentTo(File target, String content) {
        try {
            Files.append(content, target, Charsets.UTF_8);
        } catch (IOException e) {
            throw new InitializerException("It was not possible to write content to template", e);
        }
    }

    @Override
    public byte[] fileToByteArray(File file) {
        try(InputStream is = new FileInputStream(file)) {
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            throw new InitializerException("It was not possible to generate project {}", file.getName(), e);
        }
    }

    @Override
    public void copy(InputStream from, File to) {
        try(OutputStream os = new FileOutputStream(to); InputStream fromWrapper = from) {
            StreamUtils.copy(fromWrapper, os);
        } catch (IOException e) {
            throw new InitializerException("It was not possible to copy file {}", e);
        }
    }

    @Override
    public File touch(Path path) {
        try {
            File file = path.toFile();
            Files.touch(file);
            return file;
        } catch (IOException e) {
            log.error("It was not possible create file {}", path);
            throw new InitializerException("It was not possible to create file", e);
        }
    }
    
    @Override
    public void createDirectories(File file) {
        file.mkdirs();
    }

    @Override
    public InputStream loadResourceFromClassPath(String file) {
        return this.getClass()
                   .getClassLoader()
                   .getResourceAsStream(file);
    }

    @Override
    public void delete(File file) {
        boolean deleted = file.delete();
        log.info("File {} deleted : {}", file.getName(), deleted);
    }
    
    @Override
    public void moveFile(File from, File to) {
        try {
            Files.move(from, to);
        } catch (IOException e) {
            throw new InitializerException("It was not possible to move file from {} to {}",e, from, to);
        }
    }
    
    
    
}