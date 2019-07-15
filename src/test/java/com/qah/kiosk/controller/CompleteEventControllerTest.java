package com.qah.kiosk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.security.WithMockJwtAuthenticationToken;
import com.qah.kiosk.service.EventService;
import com.qah.kiosk.validators.GenericEventFullValidator;
import com.qah.kiosk.validators.GenericEventPutValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = {CompleteEventController.class})
@ContextConfiguration(classes={MainApp.class}) // see SpringBootTestContextBootstrapper
public class CompleteEventControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EventService eventService;
	
	@MockBean
	private JwtDecoder jwtDecoder;
	
	@MockBean
	private GenericEventPutValidator genericEventPutValidator;
	
	@MockBean
	private GenericEventFullValidator genericEventValidator;
	
	private final String GENERIC_EVENT = "{\"id\" : 34,"
			+ "\"eventTitle\" : \"title\","
			+ "\"eventLanguage\" : \"eventLanguage\","
			+ "\"dataLanguage\" : \"defaultDataLanguage\","
			+ "\"comments\" : \"Comments\","
			+ "\"defaultTranslationId\" : 29,"
			+ "\"eventType\" : \"CA-CO\","
			+ "\"eventStartDate\" : \"2019-03-25\","
			+ "\"eventEndDate\" : \"2019-03-28\","
			+ "\"defaultDataLang\" : \"defaultDataLanguage\","
			+ "\"eventTranslations\" : ["
						+ "{"
							+ "\"id\" : 45,"
							+ "\"eventTitle\" : \"translatedTitle\","
							+ "\"eventLanguage\" : \"evLang\","
							+ "\"dataLanguage\" : \"en\","
							+ "\"comments\" : \"newLangComment\""

						+ "}"

			
				+ "]"

			+ "}";

	
	private GenericEvent sampleEvent;
	
	@Before
	public void setup() {
		when(genericEventPutValidator.supports(GenericEvent.class)).thenReturn(true);
		when(genericEventValidator.supports(GenericEvent.class)).thenReturn(true);
		
		GenericEvent ge = new GenericEvent();
		ge.setComments("Comments");
		ge.setDataLanguage("defaultDataLanguage");
		ge.setDefaultDataLang("defaultDataLanguage");
		ge.setDefaultTranslationId(29L);
		ge.setEventEndDate(LocalDate.of(2019, 3, 28));
		ge.setEventStartDate(LocalDate.of(2019, 3, 25));
		ge.setEventLanguage("eventLanguage");
		ge.setEventTitle("title");
		ge.setEventType("CA-CO");
		ge.setId(34L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("newLangComment");
		eto.setDataLanguage("en");
		eto.setEventLanguage("evLang");
		eto.setEventTitle("translatedTitle");
		eto.setId(45L);
		
		ge.setEventTranslations(Arrays.asList(eto));
		
		sampleEvent = ge;
	}
	
	/*
	 * Valid id
	 * 
	 * Expect: 200 and body
	 */
	 
	@Test
	public void givenValidId_thenGetEvent_returnsFullyPopulatedResponse() throws Exception {
		
		when(eventService.getEvent(34L)).thenReturn(sampleEvent);
		
		mockMvc.perform(get("/event/34"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(34))
		.andExpect(jsonPath("$.comments").value("Comments"))
		.andExpect(jsonPath("$.dataLanguage").value("defaultDataLanguage"))
		.andExpect(jsonPath("$.defaultTranslationId").value(29))
		.andExpect(jsonPath("$.eventTitle").value("title"))
		.andExpect(jsonPath("$.eventStartDate").value("2019-03-25"))
		.andExpect(jsonPath("$.eventEndDate").value("2019-03-28"))
		.andExpect(jsonPath("$.eventTranslations.length()").value(1))
		.andExpect(jsonPath("$.eventTranslations[0].comments").value("newLangComment"))
		.andExpect(jsonPath("$.eventTranslations[0].dataLanguage").value("en"))
		.andExpect(jsonPath("$.eventTranslations[0].eventTitle").value("translatedTitle"))
		.andExpect(jsonPath("$.eventTranslations[0].eventLanguage").value("evLang"))
		.andExpect(jsonPath("$.eventTranslations[0].id").value(45));

	}
	
	/*
	 * Invalid id
	 * 
	 * Expects: 404
	 */

	@Test
	public void givenInvalidId_thenGetEvent_returns404() throws Exception {
		
		when(eventService.getEvent(34L)).thenReturn(null);
		
		mockMvc.perform(get("/event/34"))
		.andExpect(status().isNotFound());

	}
	
	/*
	 * GenericEvent received
	 * 
	 * Expect: 201 and body if successful
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidEvent_thenPostTranslation_objectReceived() throws Exception {
		mockMvc.perform(post("/event")
				.contentType(MediaType.APPLICATION_JSON)
				.content(GENERIC_EVENT))
				.andExpect(status().isCreated());
	
		ArgumentCaptor<GenericEvent> etoCapt = ArgumentCaptor.forClass(GenericEvent.class);
	
		verify(eventService).postEvent(etoCapt.capture());
	
		GenericEvent received = etoCapt.getValue();
	
		Assert.assertTrue(received.getComments().equals(sampleEvent.getComments()));
		Assert.assertTrue(received.getDataLanguage().equals(sampleEvent.getDataLanguage()));
		Assert.assertTrue(received.getEventLanguage().equals(sampleEvent.getEventLanguage()));
		Assert.assertTrue(received.getEventTitle().equals(sampleEvent.getEventTitle()));
		Assert.assertTrue(received.getDefaultDataLang().equals(sampleEvent.getDefaultDataLang()));
		Assert.assertTrue(received.getEventStartDate().equals(sampleEvent.getEventStartDate()));
		Assert.assertTrue(received.getEventEndDate().equals(sampleEvent.getEventEndDate()));
		Assert.assertTrue(received.getEventType().equals(sampleEvent.getEventType()));
	
	}
	
	/*
	 * Verify translations are received
	 *
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidEvent_thenPostTranslation_objectTranslationsReceived() throws Exception {
		mockMvc.perform(post("/event")
				.contentType(MediaType.APPLICATION_JSON)
				.content(GENERIC_EVENT))
				.andExpect(status().isCreated());
	
		ArgumentCaptor<GenericEvent> etoCapt = ArgumentCaptor.forClass(GenericEvent.class);
	
		verify(eventService).postEvent(etoCapt.capture());
	
		GenericEvent received = etoCapt.getValue();
	
		Assert.assertTrue(received.getEventTranslations().size() == 1);
		
		EventTranslationObject etoReceived = received.getEventTranslations().get(0);
		EventTranslationObject etoSent = sampleEvent.getEventTranslations().get(0);
		
		Assert.assertTrue(etoReceived.getDataLanguage().equals(etoSent.getDataLanguage()));
		Assert.assertTrue(etoReceived.getEventLanguage().equals(etoSent.getEventLanguage()));
		Assert.assertTrue(etoReceived.getEventTitle().equals(etoSent.getEventTitle()));
		Assert.assertTrue(etoReceived.getComments().equals(etoSent.getComments()));
	
	}
	
	/*
	 * GenericEvent received (PUT)
	 * 
	 * Expect: 204 and body if successful
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidEvent_thenPutTranslation_objectReceived() throws Exception {
		mockMvc.perform(put("/event/43")
				.contentType(MediaType.APPLICATION_JSON)
				.content(GENERIC_EVENT))
				.andExpect(status().isNoContent());
	
		ArgumentCaptor<GenericEvent> etoCapt = ArgumentCaptor.forClass(GenericEvent.class);
	
		verify(eventService).putEvent(etoCapt.capture());
	
		GenericEvent received = etoCapt.getValue();
	
		Assert.assertTrue(received.getComments().equals(sampleEvent.getComments()));
		Assert.assertTrue(received.getDataLanguage().equals(sampleEvent.getDataLanguage()));
		Assert.assertTrue(received.getEventLanguage().equals(sampleEvent.getEventLanguage()));
		Assert.assertTrue(received.getEventTitle().equals(sampleEvent.getEventTitle()));
		Assert.assertTrue(received.getDefaultDataLang().equals(sampleEvent.getDefaultDataLang()));
		Assert.assertTrue(received.getEventStartDate().equals(sampleEvent.getEventStartDate()));
		Assert.assertTrue(received.getEventEndDate().equals(sampleEvent.getEventEndDate()));
		Assert.assertTrue(received.getEventType().equals(sampleEvent.getEventType()));
	
	}

	/*
	 * Delete id received
	 * 
	 * Expect: 204 if successful
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidId_thenDeleteEvent_successful() throws Exception {
		doReturn(true).when(eventService).deleteEvent(any());

		mockMvc.perform(delete("/event/43")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNoContent());
		
		ArgumentCaptor<Long> idCapt = ArgumentCaptor.forClass(Long.class);
		verify(eventService).deleteEvent(idCapt.capture());
		
		Long received = idCapt.getValue();
		
		Assert.assertTrue(received.equals(43L));
		
	}	
	
	/*
	 * Delete non-existing id
	 * 
	 * Expect: 404 if invalid id is provided
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenInvalidId_thenDeleteTranslation_unsuccessful() throws Exception {
		when(eventService.deleteEvent(43L)).thenReturn(false);

		mockMvc.perform(delete("/event/43")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		
	}

}
