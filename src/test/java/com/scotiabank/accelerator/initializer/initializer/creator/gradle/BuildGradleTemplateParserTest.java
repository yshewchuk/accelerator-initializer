/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.gradle;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableMap;

public class BuildGradleTemplateParserTest {
    
    @Mock
    private FileProcessor fileProcessor;
    private File file;
    
    private BuildGradleTemplateParser parser;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.file = new File("./mock.build.gradle");
        this.parser = new BuildGradleTemplateParser(fileProcessor, createArgs(), file);
    }
    
    private Map<String, Object> createArgs() {
        return ImmutableMap.<String, Object>builder()
                            .put("ACCP_VERSION", "1.2.1")
                            .put("SPRING_BOOT_VERSION", "1.5.2.RELEASE")
                            .put("IS_SPRING_BOOT", true)
                            .put("REPOSITORY_NAME", "initializer")
                            .put("GROUP_ID", "com.bns.hopper")
                            .build();
    }
    
    @Test
    public void assertBuildGradleIsCalled() {
        this.parser.addBuildScript();
        verify(this.fileProcessor, times(1)).processTemplate(BuildGradleTemplateParser.BUILDSCRIPT_PATH, createArgs());
        verify(this.fileProcessor, times(1)).writeContentTo(this.file, null);
    }
    
    @Test
    public void asserAddApplyPluginIsCalled() {
        this.parser.addApplyPlugin();
        verify(this.fileProcessor, times(1)).processTemplate(BuildGradleTemplateParser.APPLY_PLUGINS_PATH, createArgs());
        verify(this.fileProcessor, times(1)).writeContentTo(this.file, null);
    }
    
    @Test
    public void asserAddDependenciesIsCalled() {
        this.parser.addDependencies();
        verify(this.fileProcessor, times(1)).processTemplate(BuildGradleTemplateParser.DEPENDENCIES_PATH, createArgs());
        verify(this.fileProcessor, times(1)).writeContentTo(this.file, null);
    }
    
    @Test
    public void asserAddJarIsCalled() {
        this.parser.addJar();
        verify(this.fileProcessor, times(1)).processTemplate(BuildGradleTemplateParser.JAR_PATH, Collections.emptyMap());
        verify(this.fileProcessor, times(1)).writeContentTo(this.file, null);
    }
    
    @Test
    public void asserAddIntegrationTestIsCalled() {
        this.parser.addIntegrationTest();
        verify(this.fileProcessor, times(1)).processTemplate(BuildGradleTemplateParser.INTEGRATION_TEST_PATH, Collections.emptyMap());
        verify(this.fileProcessor, times(1)).writeContentTo(this.file, null);
    }
    
    @Test
    public void asserAddProjectMetadatatIsCalled() {
        this.parser.addProjectMetadata();
        verify(this.fileProcessor, times(1)).processTemplate(BuildGradleTemplateParser.PROJECT_METADATA_PATH, createArgs());
        verify(this.fileProcessor, times(1)).writeContentTo(this.file, null);
    }
    
}