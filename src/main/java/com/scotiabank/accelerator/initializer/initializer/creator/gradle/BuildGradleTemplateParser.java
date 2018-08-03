/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.gradle;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;

import static com.google.common.base.Preconditions.checkNotNull;

public class BuildGradleTemplateParser {
    static final String INTEGRATION_TEST_PATH = "projectCreation/buildgradle/integrationTest.tpl";
    static final String JAR_PATH = "projectCreation/buildgradle/jar.tpl";
    static final String DEPENDENCIES_PATH = "projectCreation/buildgradle/dependencies.tpl";
    static final String REPOSITORIES_PATH = "projectCreation/buildgradle/repositories.tpl";
    static final String PROJECT_METADATA_PATH = "projectCreation/buildgradle/projectMetadata.tpl";
    static final String BUILDSCRIPT_PATH = "projectCreation/buildgradle/buildscript.tpl";
    static final String APPLY_PLUGINS_PATH = "projectCreation/buildgradle/ApplyPlugin.tpl";
    
    private final FileProcessor fileProcessor;
    private Map<String, Object> args;
    private File buildDotGradle;

    public BuildGradleTemplateParser(FileProcessor fileProcessor, Map<String, Object> args, File buildDotGradle) {
        this.fileProcessor = checkNotNull(fileProcessor);
        this.buildDotGradle = checkNotNull(buildDotGradle);
        this.args = args;
    }
    
    public BuildGradleTemplateParser addBuildScript() {
        String content = this.fileProcessor.processTemplate(BUILDSCRIPT_PATH, args);
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
    public BuildGradleTemplateParser addApplyPlugin() {
        String content = this.fileProcessor.processTemplate(APPLY_PLUGINS_PATH, args);
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
    public BuildGradleTemplateParser addProjectMetadata() {
        String content = this.fileProcessor.processTemplate(PROJECT_METADATA_PATH, args);
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
    public BuildGradleTemplateParser addRepositories() {
        String content = this.fileProcessor.processTemplate(REPOSITORIES_PATH, Collections.emptyMap());
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
    public BuildGradleTemplateParser addDependencies() {
        String content = this.fileProcessor.processTemplate(DEPENDENCIES_PATH, args);
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
    public BuildGradleTemplateParser addJar() {
        String content = this.fileProcessor.processTemplate(JAR_PATH, Collections.emptyMap());
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
    public BuildGradleTemplateParser addIntegrationTest() {
        String content = this.fileProcessor.processTemplate(INTEGRATION_TEST_PATH, Collections.emptyMap());
        this.fileProcessor.writeContentTo(buildDotGradle, content);
        return this;
    }
    
}