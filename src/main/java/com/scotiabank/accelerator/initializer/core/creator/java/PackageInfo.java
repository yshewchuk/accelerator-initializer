/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.java;

import java.io.File;
import java.nio.file.Path;

import com.scotiabank.accelerator.initializer.core.FileProcessor;

import static com.google.common.base.Preconditions.checkNotNull;

class PackageInfo {
    
    private FileProcessor fileProcessor;

    public PackageInfo(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
 
    public void create(Path path, String packageValue) {
        File applicationJavaClass = fileProcessor.touch(path);
        writeContentTo(applicationJavaClass, packageValue);
    }
    
    
    private void writeContentTo(File applicationJavaClass, String packageValue) {
        StringBuilder content = new StringBuilder();
        content.append("package ").append(packageValue).append(";");
        fileProcessor.writeContentTo(applicationJavaClass, content.toString());
    }
    
}