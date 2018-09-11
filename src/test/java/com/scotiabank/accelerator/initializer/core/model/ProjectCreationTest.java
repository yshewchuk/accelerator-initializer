package com.scotiabank.accelerator.initializer.core.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ProjectCreationTest {

    @Test
    public void getJavaPackageNameWithUpperCaseNameShouldReturnValidPackageName() {
        ProjectCreation projectCreation = ProjectCreation.builder()
                .name("Name")
                .build();

        String packageName = projectCreation.getJavaPackageName();
        assertThat(packageName).isEqualTo("name");
    }

    @Test
    public void getJavaPackageNameWithInvalidCharacterNameShouldReturnValidPackageName() {
        String packageName = ProjectCreation.builder()
                .name("app-name")
                .build()
                .getJavaPackageName();
        assertThat(packageName).isEqualTo("appname");

        packageName = ProjectCreation.builder()
                .name("app.name")
                .build()
                .getJavaPackageName();
        assertThat(packageName).isEqualTo("appname");

        packageName = ProjectCreation.builder()
                .name("name1")
                .build()
                .getJavaPackageName();
        assertThat(packageName).isEqualTo("name1");
    }

    @Test
    public void getJavaApplicationNameWithNameShouldReturnValidJavaClassName() {
        String applicationName = ProjectCreation.builder()
                .name("app-name")
                .build()
                .getJavaApplicationName();
        assertThat(applicationName).isEqualTo("AppName");

        applicationName = ProjectCreation.builder()
                .name("app.name")
                .build()
                .getJavaApplicationName();
        assertThat(applicationName).isEqualTo("AppName");
    }

    @Test
    public void packageToPathWithPackageNameShouldConvertToPath() {
        ProjectCreation projectCreation = ProjectCreation.builder().build();

        String packageName = "com";
        String path = projectCreation.packageToPath(packageName);
        assertThat(path).isEqualTo("com");

        packageName = "com.test";
        path = projectCreation.packageToPath(packageName);
        assertThat(path).isEqualTo("com" + File.separatorChar + "test");

        packageName = "com.Test";
        path = projectCreation.packageToPath(packageName);
        assertThat(path).isEqualTo("com" + File.separatorChar + "Test");
    }
}