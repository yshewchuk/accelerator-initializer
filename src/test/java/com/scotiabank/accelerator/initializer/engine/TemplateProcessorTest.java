package com.scotiabank.accelerator.initializer.engine;

import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createTempDirectory;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TemplateProcessorTest {
    private String sourceTemplateParentPath;

    private TemplateProcessor templateProcessor;

    @Value("${initializer.template-path}")
    private String sourceTemplateParent;

    @Before
    public void setUp() throws URISyntaxException {
        sourceTemplateParentPath = Paths.get(getClass().getClassLoader().getResource(sourceTemplateParent).toURI()).toString();
        templateProcessor = new TemplateProcessor(sourceTemplateParent);
    }

    @Test
    public void createApplicationWithProjectInformationShouldCallProcessMethod() {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .group("com.test")
                .name("test-boot-app")
                .build();

        templateProcessor.createApplication(projectCreation);
        assertThat(projectCreation.getName()).isNotBlank();
    }

    @Test
    public void processWithDirectoryShouldConvertAndCreateDestinationDirectory() throws IOException {
        Path temporaryPath = createTempDirectory(null);

        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .name("test-app")
                .group("com.test")
                .rootDir(temporaryPath.toString())
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath, projectCreation.getType().toString());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src/main/java/{{group}}/{{javaPackageName}}");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);

        templateProcessor.process(projectCreation, currentPath, relativePath.toString());
        assertThat(Paths.get(temporaryPath.toString(), "src/main/java/com/test/testapp").toFile().exists()).isTrue();

        FileUtils.deleteDirectory(temporaryPath.toFile());
    }

    @Test
    public void processWithJarFileShouldCopyFile() throws IOException {
        Path temporaryPath = createTempDirectory(null);

        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .name("test-app")
                .group("com.test")
                .rootDir(temporaryPath.toString())
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath, projectCreation.getType().toString());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "gradle/wrapper/gradle-wrapper.jar");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);

        templateProcessor.process(projectCreation, currentPath, relativePath.toString());

        Path destination = Paths.get(temporaryPath.toString(), "gradle/wrapper/gradle-wrapper.jar");

        assertThat(destination.toFile().exists()).isTrue();
        assertThat(destination.toFile().length()).isEqualTo(currentPath.toFile().length());

        FileUtils.deleteDirectory(temporaryPath.toFile());
    }

    @Test
    public void processWithFileShouldConvertAndCreateDestinationFile() throws IOException {
        Path temporaryPath = createTempDirectory(null);
        Paths.get(temporaryPath.toString(), "src/main/java/com/test/testapp").toFile().mkdirs();

        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .name("test-app")
                .group("com.test")
                .rootDir(temporaryPath.toString())
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath, projectCreation.getType().toString());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src/main/java/{{group}}/{{javaPackageName}}/{{javaApplicationName}}Application.java");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);

        templateProcessor.process(projectCreation, currentPath, relativePath.toString());

        Path destination = Paths.get(temporaryPath.toString(), "src/main/java/com/test/testapp/TestAppApplication.java");

        assertThat(destination.toFile().exists()).isTrue();

        String fileContent = FileUtils.readFileToString(destination.toFile(), "UTF-8");
        assertThat(fileContent).doesNotContain("{{javaApplicationName}}");

        FileUtils.deleteDirectory(temporaryPath.toFile());
    }

    @Test
    public void processRelativePathWithDirectoryContainingVariablesShouldReturnProcessedRelativePath() {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .group("com.test")
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath + projectCreation.getType());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src/{{group}}");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);
        assertThat(relativePath).isEqualTo(Paths.get("/src/com/test"));
    }

    @Test
    public void processRelativePathWithDirectoryWithoutVariablesShouldReturnRelativePath() {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath + projectCreation.getType());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);
        assertThat(relativePath).isEqualTo(Paths.get("/src"));
    }

    @Test
    public void processRelativePathWithFilenameContainingVariablesShouldReturnProcessedRelativePath() {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .group("com.test")
                .name("Demo")
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath, projectCreation.getType().toString());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src/main/java/{{group}}/{{javaPackageName}}/{{javaApplicationName}}Application.java");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);
        assertThat(relativePath).isEqualTo(Paths.get("/src/main/java/com/test/demo/DemoApplication.java"));
    }

    @Test
    public void processRelativePathWithFilenameWithoutVariablesShouldReturnRelativePath() throws IOException, URISyntaxException {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath, projectCreation.getType().toString());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "build.gradle");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);
        assertThat(relativePath).isEqualTo(Paths.get("/build.gradle"));
    }
}
