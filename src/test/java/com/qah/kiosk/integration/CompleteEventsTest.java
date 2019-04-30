package com.qah.kiosk.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.qah.kiosk.util.GenericEventListWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
@AutoConfigureMockMvc
public class CompleteEventsTest {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * GET a list of generic events successfully: 200
	 */
	@Test
	public void getExistingGenericEvent_returns200() throws Exception{
		mockMvc.perform(get("/events?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
					
	}
	
	/*
	 * GET a list of generic events successfully: number of events matches
	 */
	@Test
	public void getExistingGenericEvent_returnsBody() throws Exception{
		mockMvc.perform(get("/events?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30"))
		.andExpect(jsonPath("$.events.length()").value(5));
					
	}
	
	/*
	 * GET a list of unexisting generic events: 404
	 */
	@Test
	public void getExistingGenericEvent_returns404() throws Exception{
		mockMvc.perform(get("/events?eventLang=en&eventType=CA-CO&after=2021-03-27&before=2021-03-30"))
		.andExpect(status().isNotFound());
					
	}
	
	/*
	 * POST a valid list of GenericEvents - returns 201
	 */
	@Test
	public void postValidGenericEventList_returns201() throws Exception{
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
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setComments("Comentarios");
		ev2.setDataLanguage("es");
		ev2.setDefaultDataLang("es");
		ev2.setDefaultTranslationId(null);
		ev2.setEventEndDate(LocalDate.of(2020, 9, 28));
		ev2.setEventLanguage("ing");
		ev2.setEventStartDate(LocalDate.of(2020, 9, 25));
		ev2.setEventTitle("Sea valiente");
		ev2.setEventType("REG");
		ev2.setId(null);
		
		EventTranslationObject eto3 = new EventTranslationObject();
		eto3.setId(null);
		eto3.setEventTitle("Ser decidido");
		eto3.setEventLanguage("ing");
		eto3.setDataLanguage("pr");
		eto3.setComments("Comentarios");
		
		EventTranslationObject eto4 = new EventTranslationObject();
		eto4.setId(null);
		eto4.setEventTitle("Be courageous");
		eto4.setEventLanguage("en");
		eto4.setDataLanguage("en");
		eto4.setComments("Comments");
		
		List<EventTranslationObject> etos2 = new ArrayList<>();
		etos2.add(eto3);
		etos2.add(eto4);
		
		ev2.setEventTranslations(etos2);
		
		List<GenericEvent> listOfEvents = new ArrayList<>();
		listOfEvents.add(ev);
		listOfEvents.add(ev2);

		
		MvcResult result = mockMvc.perform(post("/events")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(new GenericEventListWrapper(listOfEvents))))
						.andExpect(status().isCreated())
						.andReturn();
		
		GenericEventListWrapper response = mapper.readValue(result.getResponse().getContentAsString(), GenericEventListWrapper.class);
		
		GenericEvent firstEvent = response.getEvents().get(0);
		// object 1 was created
		mockMvc.perform(get("/event/" + firstEvent.getId()))
			.andExpect(jsonPath("$.id").value(firstEvent.getId()))
			.andExpect(jsonPath("$.comments").value("Comentarios"))
			.andExpect(jsonPath("$.dataLanguage").value("es"))
			.andExpect(jsonPath("$.eventLanguage").value("ing"))
			.andExpect(jsonPath("$.defaultDataLang").value("es"))
			.andExpect(jsonPath("$.eventEndDate").value("2020-09-30"))
			.andExpect(jsonPath("$.eventStartDate").value("2020-09-29"))
			.andExpect(jsonPath("$.eventType").value("REG"))
			.andExpect(jsonPath("$.eventTitle").value("Sea valiente"))
			.andExpect(jsonPath("$.eventTranslations.length()").value(2));
		
		
		GenericEvent secondEvent = response.getEvents().get(1);
		// object 2 was created
		mockMvc.perform(get("/event/" + secondEvent.getId()))
			.andExpect(jsonPath("$.id").value(secondEvent.getId()))
			.andExpect(jsonPath("$.comments").value("Comentarios"))
			.andExpect(jsonPath("$.dataLanguage").value("es"))
			.andExpect(jsonPath("$.eventLanguage").value("ing"))
			.andExpect(jsonPath("$.defaultDataLang").value("es"))
			.andExpect(jsonPath("$.eventEndDate").value("2020-09-28"))
			.andExpect(jsonPath("$.eventStartDate").value("2020-09-25"))
			.andExpect(jsonPath("$.eventType").value("REG"))
			.andExpect(jsonPath("$.eventTitle").value("Sea valiente"))
			.andExpect(jsonPath("$.eventTranslations.length()").value(2));
	}
	
	/*
	 * POST an invalid list of GenericEvents - 1 of them has no eventType - returns 422
	 */
	@Test
	public void postInvalidGenericEventList_noEventType_returns422() throws Exception{
		GenericEvent ev = new GenericEvent();
		ev.setComments("Comentarios");
		ev.setDataLanguage("es");
		ev.setDefaultDataLang("es");
		ev.setDefaultTranslationId(null);
		ev.setEventEndDate(LocalDate.of(2020, 9, 30));
		ev.setEventLanguage("ing");
		ev.setEventStartDate(LocalDate.of(2020, 9, 29));
		ev.setEventTitle("Sea valiente");
		ev.setEventType(null);
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
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setComments("Comentarios");
		ev2.setDataLanguage("es");
		ev2.setDefaultDataLang("es");
		ev2.setDefaultTranslationId(null);
		ev2.setEventEndDate(LocalDate.of(2020, 9, 28));
		ev2.setEventLanguage("ing");
		ev2.setEventStartDate(LocalDate.of(2020, 9, 25));
		ev2.setEventTitle("Sea valiente");
		ev2.setEventType("REG");
		ev2.setId(null);
		
		EventTranslationObject eto3 = new EventTranslationObject();
		eto3.setId(null);
		eto3.setEventTitle("Ser decidido");
		eto3.setEventLanguage("ing");
		eto3.setDataLanguage("pr");
		eto3.setComments("Comentarios");
		
		EventTranslationObject eto4 = new EventTranslationObject();
		eto4.setId(null);
		eto4.setEventTitle("Be courageous");
		eto4.setEventLanguage("en");
		eto4.setDataLanguage("en");
		eto4.setComments("Comments");
		
		List<EventTranslationObject> etos2 = new ArrayList<>();
		etos2.add(eto3);
		etos2.add(eto4);
		
		ev2.setEventTranslations(etos2);
		
		List<GenericEvent> listOfEvents = new ArrayList<>();
		listOfEvents.add(ev);
		listOfEvents.add(ev2);

		
		mockMvc.perform(post("/events")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(new GenericEventListWrapper(listOfEvents))))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(containsString("The field eventType cannot be null")));

	}
	
	/*
	 * POST an invalid GenericEvent - duplicate event in List - returns 422
	 */
	@Test
	public void postInvalidGenericEvent_eventDuplicateInList_returns422() throws Exception{
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
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setComments("Comentarios");
		ev2.setDataLanguage("en");
		ev2.setDefaultDataLang("en");
		ev2.setDefaultTranslationId(null);
		ev2.setEventEndDate(LocalDate.of(2020, 3, 31));
		ev2.setEventLanguage("en");
		ev2.setEventStartDate(LocalDate.of(2020, 3, 29));
		ev2.setEventTitle("Be courageous");
		ev2.setEventType("REG");
		ev2.setId(null);
		
		EventTranslationObject eto3 = new EventTranslationObject();
		eto3.setId(null);
		eto3.setEventTitle("Ser decidido");
		eto3.setEventLanguage("ing");
		eto3.setDataLanguage("pr");
		eto3.setComments("Comentarios");

		
		List<EventTranslationObject> etos2 = new ArrayList<>();
		etos2.add(eto3);
		
		ev2.setEventTranslations(etos2);
		
		List<GenericEvent> listOfEvents = new ArrayList<>();
		listOfEvents.add(ev);
		listOfEvents.add(ev2);

		
		mockMvc.perform(post("/events")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(new GenericEventListWrapper(listOfEvents))))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(containsString("This event already exists in provided list")));

	}
	
	/*
	 * DELETE a list of GenericEvents: returns 204
	 */
	@Test
	public void deleteGenericEvent_returns204() throws Exception{
		
		mockMvc.perform(delete("/events?eventLang=en&eventType=REG&after=2020-03-27&before=2020-03-30"))
				.andExpect(status().isNoContent());
		
		
		mockMvc.perform(get("/event/0"))
				.andExpect(status().isNotFound());
		
		mockMvc.perform(get("/event/1"))
		.andExpect(status().isNotFound());
		
		mockMvc.perform(get("/event/2"))
		.andExpect(status().isNotFound());
		
		mockMvc.perform(get("/event/4"))
		.andExpect(status().isNotFound());
		
		mockMvc.perform(get("/event/5"))
		.andExpect(status().isNotFound());


		mockMvc.perform(get("/events?eventLang=en&eventType=REG&after=2020-03-27&before=2020-03-30"))
		.andExpect(status().isNotFound());
				
				
	}
	
	/*
	 * DELETE a list of GenericEvents that don't exist: returns 404
	 */
	@Test
	public void deleteGenericEvent_doesntExist_returns404() throws Exception{

		mockMvc.perform(delete("/events?eventLang=ko&eventType=REG&after=2020-03-27&before=2020-03-30"))
				.andExpect(status().isNotFound());
	
	}
	
	

}
