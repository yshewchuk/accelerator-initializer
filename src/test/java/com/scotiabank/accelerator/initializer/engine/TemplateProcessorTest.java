package com.scotiabank.accelerator.initializer.engine;

import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TemplateProcessorTest {
    @MockBean
    private ResourceLoader resourceLoader;

    private String sourceTemplateParentPath;

    private TemplateProcessor templateProcessor;

    @Before
    public void setUp() throws URISyntaxException {
        sourceTemplateParentPath = Paths.get(getClass().getClassLoader().getResource("templates/projectCreation").toURI()).toString();
        templateProcessor = new TemplateProcessor(resourceLoader, sourceTemplateParentPath);
    }

    @Test
    public void processRelativePathWithDirectoryContainingVariablesShouldReturnProcessedRelativePath() {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .type(ApplicationType.JAVA_SPRING_BOOT_2)
                .group("com.test")
                .build();

        Path sourceTemplatePath = Paths.get(sourceTemplateParentPath + projectCreation.getType());
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src", "{{group}}");

        Path relativePath = templateProcessor.processRelativePath(sourceTemplatePath, currentPath, projectCreation);
        assertThat(relativePath).isEqualTo(Paths.get("/src", "com", "test"));
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
        Path currentPath = Paths.get(sourceTemplatePath.toString(), "src/main/java", "{{group}}", "{{javaPackageName}}", "{{javaApplicationName}}Application.java");

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
