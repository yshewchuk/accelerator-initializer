/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.hopper.initializer;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public interface FileProcessor {
    
    /**
     * Copies content from an input stream to a file
     * @param from InputStream
     * @param to File
     */
    void copy(InputStream from, File to);
    
    /**
     * It will append content to a specific file
     * @param target
     * @param content
     */
    void writeContentTo(File target, String content);
    
    /**
     * It converts a file to a array of bytes
     * @param f
     * @return the file as array of bytes
     */
    byte[] fileToByteArray(File f);
    
    /**
     * Creates directories all parents and the file itself
     * @param file
     */
    void createDirectories(File file);
    
    /**
     * Parses a template locate under resources/template folder
     * @param templateName e.g.: projectCreation/Application.tpl
     * @param vars variables in your template
     * @return a template parsed
     */
    String processTemplate(String templateName, Map<String,Object> vars);
    
    /**
     * Creates a new File
     * @param path
     * @return The file that represents the path
     */
    File touch(Path path);
    
    /**
     * It will load a resource(file) from app's classpath
     * @param string
     * @return the file as inputStream
     */
    InputStream loadResourceFromClassPath(String string);

    void delete(File file);

    void moveFile(File from, File to);
    
}