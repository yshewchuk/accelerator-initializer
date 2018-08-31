package com.scotiabank.accelerator.initializer.controller;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Eddú Meléndez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AcceleratorHealthTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void healthEndpointIsAlwaysEnabled() throws Exception {
		this.mvc.perform(get("/actuator/health"))
				.andExpect(status().isOk());
	}

}
