package com.qah.kiosk.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
@AutoConfigureMockMvc
public class TranslationsTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	/*
	 * GET a translation successfully: 200
	 */
	@Test
	public void getExistingTranslation_returns200() throws Exception{
		mockMvc.perform(get("/translations/4"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
					
	}
	
	/*
	 * GET a translation successfully: fully populated body
	 */
	@Test
	public void getExistingTranslation_returnsBody() throws Exception{
		mockMvc.perform(get("/translations/4"))
		.andExpect(jsonPath("$.id").value(4))
		.andExpect(jsonPath("$.comments").value("Comentarios"))
		.andExpect(jsonPath("$.dataLanguage").value("sp"))
		.andExpect(jsonPath("$.eventLanguage").value("ing"))
		.andExpect(jsonPath("$.eventTitle").value("Sea valiente"));
					
	}
	
	/*
	 * POST a translation with an invalid event id - returns 422
	 */
	@Test
	public void postTranslationWithInvalidEventId_returnsBody() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		
		mockMvc.perform(post("/translations/event/41")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isUnprocessableEntity())
						.andExpect(content().string(containsString("There is no event that matches the id 41")));
					
	}
	
	/*
	 * POST a valid translation - returns 201
	 */
	@Test
	public void postValidTranslation_returns201() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		MvcResult result = mockMvc.perform(post("/translations/event/4")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isCreated())
						.andReturn();
		
		EventTranslationObject response = mapper.readValue(result.getResponse().getContentAsString(), EventTranslationObject.class);
		
		// the object was created
		mockMvc.perform(get("/translations/" + response.getId()))
			.andExpect(jsonPath("$.id").value(response.getId()))
			.andExpect(jsonPath("$.comments").value("Comentarios"))
			.andExpect(jsonPath("$.dataLanguage").value("pr"))
			.andExpect(jsonPath("$.eventLanguage").value("en"))
			.andExpect(jsonPath("$.eventTitle").value("Ser decidido"));
	}
	
	/*
	 * POST an invalid translation: id provided - returns 422
	 */
	@Test
	public void postInvalidTranslation_idProvided_returns422() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(4L);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		
		mockMvc.perform(post("/translations/event/3")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isUnprocessableEntity())
						.andExpect(content().string(containsString("The field id cannot be provided")));

					
	}
	
	/*
	 * POST an invalid translation: no eventTitle, no dataLanguage - returns 422
	 */
	@Test
	public void postInvalidTranslation_noEventTitle_returns422() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("");
		eto.setEventLanguage("en");
		eto.setDataLanguage("");
		eto.setComments(null);
		
		
		mockMvc.perform(post("/translations/event/3")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isUnprocessableEntity())
						.andExpect(content().string(containsString("The field eventTitle cannot be null")))
						.andExpect(content().string(containsString("The field dataLanguage cannot be null")));

					
	}
	
	/*
	 * POST an invalid translation: duplicate - returns 422
	 */
	@Test
	public void postInvalidTranslation_duplicate_returns422() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Be courageous - 2");
		eto.setEventLanguage("en");
		eto.setDataLanguage("en");
		eto.setComments(null);
		
		
		mockMvc.perform(post("/translations/event/0")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isUnprocessableEntity())
						.andExpect(content().string(containsString("already exists")));

					
	}
	
	/*
	 * PUT a valid translation - returns 204
	 */
	@Test
	public void putValidTranslation_returns204() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		mockMvc.perform(put("/translations/7")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isNoContent());
					
	}
	
	/*
	 * PUT an invalid translation: cannot change defaultDataLang - returns 204
	 */
	@Test
	public void putValidTranslation_changeDefaultDataLang_returns422() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		mockMvc.perform(put("/translations/6")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(eto)))
						.andExpect(status().isUnprocessableEntity())
						.andExpect(content().string(containsString("The dataLanguage of the existing translation cannot be changed")));
					
	}
	
	/*
	 * DELETE a translation: returns 204
	 */
	@Test
	public void deleteTranslation_returns204() throws Exception{
		
		mockMvc.perform(delete("/translations/7"))
				.andExpect(status().isNoContent());
		
		
		mockMvc.perform(get("/translations/7"))
				.andExpect(status().isNotFound());
				
				
	}
	
	/*
	 * DELETE a translation that doesn't exist: returns 404
	 */
	@Test
	public void deleteTranslation_doesntExist_returns404() throws Exception{

		mockMvc.perform(delete("/translations/9999"))
				.andExpect(status().isNotFound());
	
	}
	
}
