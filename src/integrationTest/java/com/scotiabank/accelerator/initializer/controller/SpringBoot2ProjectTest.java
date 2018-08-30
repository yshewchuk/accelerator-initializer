package com.scotiabank.accelerator.initializer.controller;

import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.engine.TemplateProcessor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.scotiabank.accelerator.initializer.controller.ExecutionTimeVerifier.executionTimeLessThan;
import static com.scotiabank.accelerator.initializer.controller.ZipVerifier.isValidZip;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SpringBoot2ProjectTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TemplateProcessor templateProcessor;

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {};

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert") // uses MockMVC assertions
    public void whenDownloadBodyIsOkThenExpect201() throws Exception {
        doNothing().when(templateProcessor).createApplication(ProjectCreation.builder().build());

        this.mvc.perform(post("/api/project/generate")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{\"group\" : \"com.test\", \"type\" : \"JAVA_SPRING_BOOT_2\", \"name\": \"hopper-intake\"}")
        )
            .andExpect(executionTimeLessThan(stopwatch, 1500))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(isValidZip(tempFolder));
    }
}
