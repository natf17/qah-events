package com.qah.kiosk.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.config.MainApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
@AutoConfigureMockMvc
public class CompleteEventsTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * GET a list of complete events successfully: 200
	 */
	@Test
	public void getExistingGenericEvent_returns200() throws Exception{
		mockMvc.perform(get("/events?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
					
	}

}
