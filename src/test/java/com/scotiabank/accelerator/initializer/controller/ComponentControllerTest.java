package com.scotiabank.accelerator.initializer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scotiabank.accelerator.initializer.controller.request.ProjectProperties;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.ProjectCreationService;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ComponentController.class)
public class ComponentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectCreationService projectCreationService;

    @MockBean
    private FileProcessor fileProcessor;

    @Test(timeout = 1500L)
    public void userDownloadShouldReturn200Status() throws Exception {
        ProjectProperties projectProperties = new ProjectProperties();
        projectProperties.setGroup("com.test");
        projectProperties.setName("hopper-intake");
        projectProperties.setType(ApplicationType.JAVA_SPRING_BOOT_2);

        when(projectCreationService.create(any(ProjectCreation.class))).thenReturn(null);
        doNothing().when(fileProcessor).createDirectories(any(File.class));

        this.mvc.perform(post("/api/project/generate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(projectProperties)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        verify(projectCreationService, times(1)).create(any(ProjectCreation.class));
    }
}