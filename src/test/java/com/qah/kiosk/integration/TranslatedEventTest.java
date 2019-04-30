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
public class TranslatedEventTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * GET a translated event successfully: 200
	 */
	@Test
	public void getExistingGenericEvent_returns200() throws Exception{
		mockMvc.perform(get("/event/translated/1?lang=sp"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
					
	}
	
	/*
	 * GET a translated event successfully: fully populated body
	 */
	@Test
	public void getExistingTranslation_returnsBody() throws Exception{
		mockMvc.perform(get("/event/translated/1?lang=sp"))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.comments").value("Comentarios"))
		.andExpect(jsonPath("$.eventType").value("REG"))
		.andExpect(jsonPath("$.defaultDataLang").value("en"))
		.andExpect(jsonPath("$.dataLanguage").value("sp"))
		.andExpect(jsonPath("$.eventLanguage").value("ing"))
		.andExpect(jsonPath("$.currentTranslationId").value(4))
		.andExpect(jsonPath("$.eventTitle").value("Sea valiente"))
		.andExpect(jsonPath("$.eventStartDate").value("2020-03-29"))
		.andExpect(jsonPath("$.eventEndDate").value("2020-03-31"));
			
	}
	
	/*
	 * GET a translated event that doesn't exist: 404
	 */
	@Test
	public void getNonExistingTranslation_returns404() throws Exception{
		mockMvc.perform(get("/event/translated/99?lang=sp"))
		.andExpect(status().isNotFound());
			
	}
	
	/*
	 * GET a translated event that in language that doesn't exist: 404
	 */
	@Test
	public void getExistingTranslation_languageDoesntExist_returns404() throws Exception{
		mockMvc.perform(get("/event/translated/1?lang=ko"))
		.andExpect(status().isNotFound());
			
	}
}
