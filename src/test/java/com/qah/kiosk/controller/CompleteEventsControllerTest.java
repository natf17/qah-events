package com.qah.kiosk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.service.EventsService;
import com.qah.kiosk.service.utils.TimeRange;
import com.qah.kiosk.util.EventType;
import com.qah.kiosk.util.GenericEventListWrapper;
import com.qah.kiosk.validators.EventListValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = {CompleteEventsController.class})
@ContextConfiguration(classes={MainApp.class}) // see SpringBootTestContextBootstrapper
public class CompleteEventsControllerTest {
	
	@MockBean
	private EventsService eventsService;
	
	@MockBean
	private EventListValidator eventListValidator;
	
	@Autowired
	private MockMvc mockMvc;
	
	private GenericEvent sampleEvent1;
	
	private GenericEvent sampleEvent2;
	
	public static final String POST_CACO = "{"
			+ "\"events\" : ["
				+ "{"
					+"\"eventStartDate\" : \"2018-09-16\","
					+"\"eventEndDate\" : \"2018-09-22\","
					+ "\"eventType\" : \"CA-CO\","
					+ "\"eventTitle\" : \"Be decisive\","
					+ "\"eventLanguage\" : \"sp\","
					+ "\"dataLanguage\" : \"en\","
					+ "\"defaultDataLang\" : \"en\","
					+ "\"comments\" : \"First assembly\","
					+ "\"eventTranslations\" : ["
						+ "{"
							+ "\"eventTitle\" : \"Sea decidido\","
							+ "\"eventLanguage\" : \"esp\","
							+ "\"dataLanguage\" : \"sp\","
							+ "\"comments\" : \"Primera asamblea\""
						+ "},"
						+ "{"
							+ "\"eventTitle\" : \"Sois decidée\","
							+ "\"eventLanguage\" : \"esp\","
							+ "\"dataLanguage\" : \"fr\","
							+ "\"comments\" : \"Prémiere assamblée\""
						+ "}"
					+ "]"
				+ "},"
				+ "{"
					+"\"eventStartDate\" : \"2018-09-20\","
					+"\"eventEndDate\" : \"2018-09-24\","
					+ "\"eventType\" : \"CA-CO\","
					+ "\"eventTitle\" : \"Be decisive\","
					+ "\"eventLanguage\" : \"sp\","
					+ "\"dataLanguage\" : \"en\","
					+ "\"defaultDataLang\" : \"en\","
					+ "\"comments\" : \"First assembly\","
					+ "\"eventTranslations\" : ["
						+ "{"
							+ "\"eventTitle\" : \"Sea decidido\","
							+ "\"eventLanguage\" : \"esp\","
							+ "\"dataLanguage\" : \"sp\","
							+ "\"comments\" : \"Primera asamblea\""
						+ "},"
						+ "{"
							+ "\"eventTitle\" : \"Sois decidée\","
							+ "\"eventLanguage\" : \"esp\","
							+ "\"dataLanguage\" : \"fr\","
							+ "\"comments\" : \"Prémiere assamblée\""
						+ "}"
					+ "]"
				+ "}"

			+ "]"
				
		+ "}";
	
	@Before
	public void setup() {
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
		
		sampleEvent1 = ge;
		
		// same as ge, but with different dates
		GenericEvent ge2 = new GenericEvent();
		ge2.setComments("Comments");
		ge2.setDataLanguage("defaultDataLanguage");
		ge2.setDefaultDataLang("defaultDataLanguage");
		ge2.setDefaultTranslationId(29L);
		ge2.setEventEndDate(LocalDate.of(2019, 3, 25));
		ge2.setEventStartDate(LocalDate.of(2019, 3, 24));
		ge2.setEventLanguage("eventLanguage");
		ge2.setEventTitle("title");
		ge2.setEventType("CA-CO");
		ge2.setId(34L);
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setComments("newLangComment");
		eto2.setDataLanguage("en");
		eto2.setEventLanguage("evLang");
		eto2.setEventTitle("translatedTitle");
		eto2.setId(45L);
		
		ge2.setEventTranslations(Arrays.asList(eto2));
		
		sampleEvent2 = ge2;
		
		when(eventListValidator.supports(GenericEventListWrapper.class)).thenReturn(true);
	}

	/*
	 * Valid parameters; verify that they're received
	 * 
	 * Expect: service receives parameters
	 * 
	 */
	@Test
	public void givenValidParams_thenGetAllEvents_paramsResolved() throws Exception {
		mockMvc.perform(get("/events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-18"))
				.andExpect(content()
								.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	
		ArgumentCaptor<String> eventLangCapt = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<EventType> eventTypeCapt = ArgumentCaptor.forClass(EventType.class);
		ArgumentCaptor<TimeRange> timeRangeCapt = ArgumentCaptor.forClass(TimeRange.class);
	
		verify(eventsService).processGet(timeRangeCapt.capture(), eventLangCapt.capture(), eventTypeCapt.capture());
	
	
		Assert.assertTrue(eventLangCapt.getValue().equals("en"));
		Assert.assertTrue(eventTypeCapt.getValue().toString().equals("REG"));
		Assert.assertTrue(timeRangeCapt.getValue().getEventStartDate().equals(LocalDate.of(2018, 7, 01)));
		Assert.assertTrue(timeRangeCapt.getValue().getEventEndDate().equals(LocalDate.of(2018, 8, 18)));
		

	}
	
	/*
	 * Valid parameters; verify that body is returned
	 * 
	 * Expect: 200 and body
	 * 
	 */
	@Test
	public void givenValidParams_thenGetAllEvents_returnsFullyPopulatedResponse() throws Exception {
		List<GenericEvent> events = new ArrayList<>(Arrays.asList(sampleEvent1, sampleEvent2));
		System.out.println(sampleEvent1.getEventStartDate());
		
		doReturn(events).when(eventsService).processGet(any(), any(), any());
		
		mockMvc.perform(get("/events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-18"))
		.andExpect(status().isOk())
		.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.events.size()").value(2))	
		.andExpect(jsonPath("$.events[0].eventStartDate").value("2019-03-25"))	
		.andExpect(jsonPath("$.events[0].eventEndDate").value("2019-03-28"))	
		.andExpect(jsonPath("$.events[0].eventType").value("CA-CO"))	
		.andExpect(jsonPath("$.events[0].eventTranslations[0].eventTitle").value("translatedTitle"))	
		.andExpect(jsonPath("$.events[1].eventStartDate").value("2019-03-24"))	
		.andExpect(jsonPath("$.events[1].eventEndDate").value("2019-03-25"))	
		.andExpect(jsonPath("$.events[1].eventType").value("CA-CO"))	
		.andExpect(jsonPath("$.events[1].eventTranslations[0].eventTitle").value("translatedTitle"));
	}
	
	/*
	 * No events found
	 * 
	 * Expects: 404
	 */
	@Test
	public void givenNoneFoundEmptyList_thenGetAllEvents_returns404() throws Exception {
		List<GenericEvent> events = new ArrayList<>();
		
		doReturn(events).when(eventsService).processGet(any(), any(), any());
		
		mockMvc.perform(get("/events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-18"))
		.andExpect(status().isNotFound());
		
	}
	
	
	
	/*
	 * GenericEventListWrapper received
	 * 
	 * Expect: 201 and body if successful
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void givenValidBody_thenPostEventsWithTranslations_isSuccessful() throws Exception {
		ArgumentCaptor<List<GenericEvent>> eventsReceivedCapt = ArgumentCaptor.forClass(List.class);
				
		mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON).content(POST_CACO))
					.andExpect(status().isCreated());
		
		verify(eventsService).saveGenericEventsList(eventsReceivedCapt.capture());

		List<GenericEvent> eventsReceived = eventsReceivedCapt.getValue();
		
		Assert.assertEquals(2,  eventsReceived.size());
		
		GenericEvent ev1 = eventsReceived.get(0);
		GenericEvent ev2 = eventsReceived.get(1);
		
		Assert.assertEquals("Be decisive", ev1.getEventTitle());
		Assert.assertEquals("en", ev1.getDataLanguage());
		Assert.assertEquals("First assembly", ev1.getComments());
		Assert.assertEquals("en", ev1.getDefaultDataLang());
		Assert.assertEquals("sp", ev1.getEventLanguage());
		Assert.assertEquals("CA-CO", ev1.getEventType());
		Assert.assertEquals(LocalDate.of(2018, 9, 16), ev1.getEventStartDate());
		Assert.assertEquals(LocalDate.of(2018, 9, 22), ev1.getEventEndDate());
		Assert.assertEquals(2, ev1.getEventTranslations().size());
		
		Assert.assertEquals("Be decisive", ev2.getEventTitle());
		Assert.assertEquals("en", ev2.getDataLanguage());
		Assert.assertEquals("First assembly", ev2.getComments());
		Assert.assertEquals("en", ev2.getDefaultDataLang());
		Assert.assertEquals("sp", ev2.getEventLanguage());
		Assert.assertEquals("CA-CO", ev2.getEventType());
		Assert.assertEquals(LocalDate.of(2018, 9, 20), ev2.getEventStartDate());
		Assert.assertEquals(LocalDate.of(2018, 9, 24), ev2.getEventEndDate());
		Assert.assertEquals(2, ev2.getEventTranslations().size());
		
		
	}
	
	/*
	 * Verify params are received...
	 * 
	 * Expect: 204 if successful
	 */
	@Test
	public void givenValidParams_thenDeleteEvents_isSuccessful() throws Exception {
		
		doReturn(true).when(eventsService).processDelete(any(), any(), any());
		
		mockMvc.perform(delete("/events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-18"))
						.andExpect(status().isNoContent());
		

		ArgumentCaptor<String> eventLangCapt = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<EventType> eventTypeCapt = ArgumentCaptor.forClass(EventType.class);
		ArgumentCaptor<TimeRange> timeRangeCapt = ArgumentCaptor.forClass(TimeRange.class);

		verify(eventsService).processDelete(timeRangeCapt.capture(), eventLangCapt.capture(), eventTypeCapt.capture());


		Assert.assertTrue(eventLangCapt.getValue().equals("en"));
		Assert.assertTrue(eventTypeCapt.getValue().toString().equals("REG"));
		Assert.assertTrue(timeRangeCapt.getValue().getEventStartDate().equals(LocalDate.of(2018, 7, 01)));
		Assert.assertTrue(timeRangeCapt.getValue().getEventEndDate().equals(LocalDate.of(2018, 8, 18)));

		
	}
	
	/*
	 * Delete params received and no matching events found
	 * 
	 * Expect: 404 
	 */
	@Test
	public void givenNoMatchingEvents_thenDeleteAllEvents_returns404() throws Exception {
		doReturn(false).when(eventsService).processDelete(any(), any(), any());

		mockMvc.perform(delete("/events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-18"))
					.andExpect(status().isNotFound());
	}
	
}
