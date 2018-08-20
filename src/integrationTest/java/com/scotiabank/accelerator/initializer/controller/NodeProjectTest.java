package com.scotiabank.accelerator.initializer.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.scotiabank.accelerator.initializer.controller.ExecutionTimeVerifier.executionTimeLessThan;
import static com.scotiabank.accelerator.initializer.controller.ZipVerifier.isValidZip;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class NodeProjectTest {
    @Autowired
    private MockMvc mvc;

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {};

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert") // uses MockMVC assertions
    public void whenDownloadBodyIsOkThenExpect201() throws Exception {
        this.mvc.perform(post("/api/project/generate")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{\"group\" : \"HOPPER\", \"type\" : \"NODE\", \"name\": \"hopper-intake\"}")
        )
            .andExpect(executionTimeLessThan(stopwatch, 1500))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(isValidZip(tempFolder));
    }
}
