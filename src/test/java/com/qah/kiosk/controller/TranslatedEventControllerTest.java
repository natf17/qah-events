package com.qah.kiosk.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.service.EventService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers={TranslatedEventController.class})
@ContextConfiguration(classes = {MainApp.class})
public class TranslatedEventControllerTest {
	
	@MockBean
	private EventService eventService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private SingleTranslatedEvent sampleEvent;
	
	@Before
	public void setup() {
		sampleEvent = new SingleTranslatedEvent();
		sampleEvent.setComments("comments");
		sampleEvent.setCurrentTranslationId(567L);
		sampleEvent.setDataLanguage("sp");
		sampleEvent.setDefaultDataLang("sp");
		sampleEvent.setEventEndDate(LocalDate.of(2018, 7, 8));
		sampleEvent.setEventStartDate(LocalDate.of(2018, 7, 6));
		sampleEvent.setEventLanguage("en");
		sampleEvent.setEventTitle("Title");
		sampleEvent.setEventType("REG");
		sampleEvent.setId(6543L);
	}	
	
	/*
	 * Given valid id, event returned in default language
	 * 
	 * Expects 200
	 */

	@Test
	public void givenValidId_thenGetEventById_eventInDefaultLanguage() throws Exception {
		
		when(eventService.getTranslatedEvent(100L, "en")).thenReturn(sampleEvent);
		
		mockMvc.perform(get("/event/translated/100")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(content()
										.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.id").value(6543))
						.andExpect(jsonPath("$.comments").value("comments"))
						.andExpect(jsonPath("$.dataLanguage").value("sp"))
						.andExpect(jsonPath("$.eventLanguage").value("en"))
						.andExpect(jsonPath("$.eventTitle").value("Title"))
						.andExpect(jsonPath("$.eventType").value("REG"))
						.andExpect(jsonPath("$.currentTranslationId").value(567))
						.andExpect(jsonPath("$.defaultDataLang").value("sp"))
						.andExpect(jsonPath("$.eventStartDate").value("2018-07-06"))
						.andExpect(jsonPath("$.eventEndDate").value("2018-07-08"));
	}
	
	/*
	 * Given valid id and language correct event returned
	 * 
	 * Expects 200
	 */
	@Test
	public void givenValidId_thenGetEventById_eventInDesiredLanguage() throws Exception {
		
		when(eventService.getTranslatedEvent(100L, "sp")).thenReturn(sampleEvent);
		
		mockMvc.perform(get("/event/translated/100?lang=sp")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(content()
										.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.id").value(6543))
						.andExpect(jsonPath("$.comments").value("comments"))
						.andExpect(jsonPath("$.dataLanguage").value("sp"))
						.andExpect(jsonPath("$.eventLanguage").value("en"))
						.andExpect(jsonPath("$.eventTitle").value("Title"))
						.andExpect(jsonPath("$.eventType").value("REG"))
						.andExpect(jsonPath("$.currentTranslationId").value(567))
						.andExpect(jsonPath("$.defaultDataLang").value("sp"))
						.andExpect(jsonPath("$.eventStartDate").value("2018-07-06"))
						.andExpect(jsonPath("$.eventEndDate").value("2018-07-08"));
	}
	
	/*
	 * If event doesn't exist...
	 * 404
	 */
	@Test
	public void givenInvalidId_thenGetEventById_returns404() throws Exception {
		
		when(eventService.getTranslatedEvent(100L, "sp")).thenReturn(null);
		
		mockMvc.perform(get("/event/translated/100?lang=sp")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound());
	}

}
