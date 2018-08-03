/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.initializer.creator.gradle;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.scotiabank.accelerator.initializer.initializer.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.initializer.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.initializer.model.ProjectCreation;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.initializer.FileProcessor;
import com.scotiabank.accelerator.initializer.initializer.creator.FileCreator;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@SpringBoot
@JavaLibrary
class GradleBinaryCreator implements FileCreator<ProjectCreation> {

    static final String GRADLE_WRAPPER_PROPERTIES_TEMPLATE_PATH = "projectCreation/gradle/gradle-wrapper.properties.tpl";
    static final String GRADLEW_PATH = "templates/projectCreation/gradle/gradlew";
    static final String GRADLEW_BAT_PATH = "templates/projectCreation/gradle/gradlew.bat";
    static final String GRADLE_WRAPPER_JAR_PATH = "templates/projectCreation/gradle/gradle-wrapper.jar";
    private final FileProcessor fileProcessor;

    public GradleBinaryCreator(FileProcessor fileProcessor) {
        this.fileProcessor = checkNotNull(fileProcessor);
    }
    
    @Override
    public void create(ProjectCreation request) {
        Path gradleFolder = Paths.get(request.getRootDir(), "gradle", "wrapper");
        gradleFolder.toFile().mkdirs();

        String gradleWrapperContent = fileProcessor.processTemplate(GRADLE_WRAPPER_PROPERTIES_TEMPLATE_PATH, ImmutableMap.of("GRADLE_VERSION", resolveGradleVersion(request)));
        this.fileProcessor.writeContentTo(gradleFolder.resolve("gradle-wrapper.properties").toFile(), gradleWrapperContent);

        File root = new File(request.getRootDir());
        InputStream gradlew = this.fileProcessor.loadResourceFromClassPath(GRADLEW_PATH);
        InputStream gradlewBat = this.fileProcessor.loadResourceFromClassPath(GRADLEW_BAT_PATH);
        InputStream gradleWrapperJar = this.fileProcessor.loadResourceFromClassPath(GRADLE_WRAPPER_JAR_PATH);

        this.fileProcessor.copy(gradlew, root.toPath().resolve("gradlew").toFile());
        this.fileProcessor.copy(gradlewBat, root.toPath().resolve("gradlew.bat").toFile());
        this.fileProcessor.copy(gradleWrapperJar, root.toPath().resolve("gradle").resolve("wrapper").resolve("gradle-wrapper.jar").toFile());
    }

    private String resolveGradleVersion(ProjectCreation request) {
        switch(request.getType()) {
            case JAVA_SPRING_BOOT_2:
                return "4.5.1";
            default:
                return "3.5.1";
        }
    }
}