package com.qah.kiosk.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.qah.kiosk.domain.GenericEvent;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
@AutoConfigureMockMvc
public class CompleteEventTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	/*
	 * GET a complete event successfully: 200
	 */
	@Test
	public void getExistingGenericEvent_returns200() throws Exception{
		mockMvc.perform(get("/event/1"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
					
	}
	
	/*
	 * GET a translation successfully: fully populated body
	 */
	@Test
	public void getExistingTranslation_returnsBody() throws Exception{
		mockMvc.perform(get("/event/1"))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.comments").value("Comments"))
		.andExpect(jsonPath("$.eventType").value("REG"))
		.andExpect(jsonPath("$.defaultDataLang").value("en"))
		.andExpect(jsonPath("$.dataLanguage").value("en"))
		.andExpect(jsonPath("$.defaultTranslationId").value(3))
		.andExpect(jsonPath("$.eventTitle").value("Be courageous"))
		.andExpect(jsonPath("$.eventStartDate").value("2020-03-29"))
		.andExpect(jsonPath("$.eventEndDate").value("2020-03-31"))
		.andExpect(jsonPath("$.eventTranslations.length()").value(2));
			
	}
	
	/*
	 * POST a valid GenericEvent - returns 201
	 */
	@Test
	public void postValidGenericEvent_returns201() throws Exception{
		GenericEvent ev = new GenericEvent();
		ev.setComments("Comentarios");
		ev.setDataLanguage("es");
		ev.setDefaultDataLang("es");
		ev.setDefaultTranslationId(null);
		ev.setEventEndDate(LocalDate.of(2020, 9, 30));
		ev.setEventLanguage("ing");
		ev.setEventStartDate(LocalDate.of(2020, 9, 29));
		ev.setEventTitle("Sea valiente");
		ev.setEventType("REG");
		ev.setId(null);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("ing");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setId(null);
		eto2.setEventTitle("Be courageous");
		eto2.setEventLanguage("en");
		eto2.setDataLanguage("en");
		eto2.setComments("Comments");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		etos.add(eto2);
		
		ev.setEventTranslations(etos);

		
		MvcResult result = mockMvc.perform(post("/event")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(ev)))
						.andExpect(status().isCreated())
						.andReturn();
		
		GenericEvent response = mapper.readValue(result.getResponse().getContentAsString(), GenericEvent.class);
		
		// the object was created
		mockMvc.perform(get("/event/" + response.getId()))
			.andExpect(jsonPath("$.id").value(response.getId()))
			.andExpect(jsonPath("$.comments").value("Comentarios"))
			.andExpect(jsonPath("$.dataLanguage").value("es"))
			.andExpect(jsonPath("$.eventLanguage").value("ing"))
			.andExpect(jsonPath("$.defaultDataLang").value("es"))
			.andExpect(jsonPath("$.eventEndDate").value("2020-09-30"))
			.andExpect(jsonPath("$.eventStartDate").value("2020-09-29"))
			.andExpect(jsonPath("$.eventType").value("REG"))
			.andExpect(jsonPath("$.eventTitle").value("Sea valiente"));
	}
	
	/*
	 * POST an invalid GenericEvent - no eventTitle - returns 422
	 */
	@Test
	public void postInvalidGenericEvent_noEventTitle_returns422() throws Exception{
		GenericEvent ev = new GenericEvent();
		ev.setComments("Comentarios");
		ev.setDataLanguage("es");
		ev.setDefaultDataLang("es");
		ev.setDefaultTranslationId(null);
		ev.setEventEndDate(LocalDate.of(2020, 9, 30));
		ev.setEventLanguage("ing");
		ev.setEventStartDate(LocalDate.of(2020, 9, 29));
		ev.setEventTitle(null);
		ev.setEventType("REG");
		ev.setId(null);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("ing");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setId(null);
		eto2.setEventTitle("Be courageous");
		eto2.setEventLanguage("en");
		eto2.setDataLanguage("en");
		eto2.setComments("Comments");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		etos.add(eto2);
		
		ev.setEventTranslations(etos);

		
		mockMvc.perform(post("/event")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(ev)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(containsString("The field eventTitle cannot be null")));
;
	}
	
	/*
	 * POST an invalid GenericEvent - event already exists - returns 422
	 */
	@Test
	public void postInvalidGenericEvent_eventAlreadyExists_returns422() throws Exception{
		GenericEvent ev = new GenericEvent();
		ev.setComments("Comentarios");
		ev.setDataLanguage("es");
		ev.setDefaultDataLang("es");
		ev.setDefaultTranslationId(null);
		ev.setEventEndDate(LocalDate.of(2020, 3, 31));
		ev.setEventLanguage("ing");
		ev.setEventStartDate(LocalDate.of(2020, 3, 29));
		ev.setEventTitle("Sea valiente");
		ev.setEventType("REG");
		ev.setId(null);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("ing");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setId(null);
		eto2.setEventTitle("Be courageous");
		eto2.setEventLanguage("en");
		eto2.setDataLanguage("en");
		eto2.setComments("Comments");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		etos.add(eto2);
		
		ev.setEventTranslations(etos);

		
		mockMvc.perform(post("/event")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(ev)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(containsString("This event already exists in the repository")));

	}
	
	/*
	 * PUT a valid GenericEvent - returns 204
	 */
	@Test
	public void putValidGenericEvent_returns201() throws Exception{
		GenericEvent ev = new GenericEvent();
		ev.setComments("Comentarios");
		ev.setDataLanguage("es");
		ev.setDefaultDataLang("es");
		ev.setDefaultTranslationId(null);
		ev.setEventEndDate(LocalDate.of(2020, 9, 30));
		ev.setEventLanguage("ing");
		ev.setEventStartDate(LocalDate.of(2020, 9, 29));
		ev.setEventTitle("Sea valiente");
		ev.setEventType("CA-CO");
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("ing");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setId(null);
		eto2.setEventTitle("Be courageous");
		eto2.setEventLanguage("en");
		eto2.setDataLanguage("en");
		eto2.setComments("Comments");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		etos.add(eto2);
		
		ev.setEventTranslations(etos);

		
		MvcResult result = mockMvc.perform(put("/event/2")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(ev)))
						.andExpect(status().isNoContent())
						.andReturn();
		
		GenericEvent response = mapper.readValue(result.getResponse().getContentAsString(), GenericEvent.class);
		
		// the object was created
		mockMvc.perform(get("/event/" + response.getId()))
			.andExpect(jsonPath("$.id").value(response.getId()))
			.andExpect(jsonPath("$.comments").value("Comentarios"))
			.andExpect(jsonPath("$.dataLanguage").value("es"))
			.andExpect(jsonPath("$.eventLanguage").value("ing"))
			.andExpect(jsonPath("$.defaultDataLang").value("es"))
			.andExpect(jsonPath("$.eventEndDate").value("2020-09-30"))
			.andExpect(jsonPath("$.eventStartDate").value("2020-09-29"))
			.andExpect(jsonPath("$.eventType").value("CA-CO"))
			.andExpect(jsonPath("$.eventTitle").value("Sea valiente"));
	}
	
	/*
	 * PUT an invalid GenericEvent - invalid translation - returns 422
	 */
	@Test
	public void putInvalidGenericEvent_returns422() throws Exception{
		GenericEvent ev = new GenericEvent();
		ev.setComments("Comentarios");
		ev.setDataLanguage("es");
		ev.setDefaultDataLang("es");
		ev.setDefaultTranslationId(null);
		ev.setEventEndDate(LocalDate.of(2020, 9, 30));
		ev.setEventLanguage("ing");
		ev.setEventStartDate(LocalDate.of(2020, 9, 29));
		ev.setEventTitle("Sea valiente");
		ev.setEventType("CA-CO");
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("ing");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setId(null);
		eto2.setEventTitle(null);
		eto2.setEventLanguage("en");
		eto2.setDataLanguage("en");
		eto2.setComments("Comments");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		etos.add(eto2);
		
		ev.setEventTranslations(etos);

		
		mockMvc.perform(put("/event/2")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(ev)))
						.andExpect(status().isUnprocessableEntity());
		
	}
	

	/*
	 * DELETE a GenericEvent: returns 204
	 */
	@Test
	public void deleteGenericEvent_returns204() throws Exception{
		
		mockMvc.perform(delete("/event/10"))
				.andExpect(status().isNoContent());
		
		
		mockMvc.perform(get("/event/10"))
				.andExpect(status().isNotFound());
				
				
	}
	
	/*
	 * DELETE a translation that doesn't exist: returns 404
	 */
	@Test
	public void deleteeGenericEvent_doesntExist_returns404() throws Exception{

		mockMvc.perform(delete("/event/9999"))
				.andExpect(status().isNotFound());
	
	}
	
	
	
	
	
	
}
