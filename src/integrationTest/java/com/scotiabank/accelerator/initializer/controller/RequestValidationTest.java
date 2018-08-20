/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RequestValidationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert") // uses MockMVC assertions
    public void whenProjectNameIsInvalidExpect400() throws Exception {
        this.mvc.perform(post("/api/project/generate")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{\"group\" : \"\", \"type\" : \"JAVA_SPRING_BOOT\", \"name\": \"$INVALID_ONE\"}")
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[*].field").value(hasItems("name", "group")))
            .andExpect(jsonPath("$.errors[*].message").value(hasItems("Name must start with a lower-case letter or number and may contain hyphens, underscores and periods")));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert") // uses MockMVC assertions
    public void whenProjectTypeUnrecognizedExpect400() throws Exception {
        this.mvc.perform(post("/api/project/generate")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content("{\"group\" : \"\", \"type\" : \"Unknown_type\", \"name\": \"VALID_ONE\"}")
        )
            .andExpect(status().isBadRequest());
    }
}
