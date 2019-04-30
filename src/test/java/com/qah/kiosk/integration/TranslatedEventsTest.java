package com.qah.kiosk.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class TranslatedEventsTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * GET a list of translated events successfully: 200
	 */
	@Test
	public void getExistingGenericEvent_returns200() throws Exception{
		mockMvc.perform(get("/events/translated?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30&lang=sp"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
					
	}
	
	/*
	 * GET a list of translated events successfully: fully populated body
	 */
	@Test
	public void getExistingTranslation_returnsBody() throws Exception{
		mockMvc.perform(get("/events/translated?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30&lang=sp"))
		.andExpect(jsonPath("$.events.length()").value(5))
		.andExpect(jsonPath("$.events[0].dataLanguage").value("sp"))
		.andExpect(jsonPath("$.events[1].dataLanguage").value("sp"))
		.andExpect(jsonPath("$.events[2].dataLanguage").value("sp"))
		.andExpect(jsonPath("$.events[3].dataLanguage").value("sp"))
		.andExpect(jsonPath("$.events[4].dataLanguage").value("sp"));
			
	}
	
	/*
	 * GET a list of translated events that don't exist: 404
	 */
	@Test
	public void getNonExistingTranslation_returns404() throws Exception{
		mockMvc.perform(get("/events/translated?eventLang=en&eventType=CA-CO&after=2021-03-27&before=2021-03-30&lang=en"))
		.andExpect(status().isNotFound());
			
	}
	
	/*
	 * GET a list of translated events in a language that doesn't exist: 404
	 */
	@Test
	public void getExistingTranslation_languageDoesntExist_returns404() throws Exception{
		mockMvc.perform(get("/events/translated?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30&lang=ko"))
		.andExpect(status().isNotFound());
			
	}
}
