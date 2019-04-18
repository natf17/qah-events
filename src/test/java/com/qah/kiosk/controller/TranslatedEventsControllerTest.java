package com.qah.kiosk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
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
import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.service.EventsService;
import com.qah.kiosk.service.utils.TimeRange;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers={TranslatedEventsController.class})
@ContextConfiguration(classes = {MainApp.class})
public class TranslatedEventsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventsService eventsService;

	/*
	 * LocalDate arguments resolved
	 */
	@Test
	public void givenLocalDate_thenGetAllEvents_createsArguments() throws Exception {
				
		ArgumentCaptor<TimeRange> timeRange = ArgumentCaptor.forClass(TimeRange.class);
		
		mockMvc.perform(get("/events/translated?eventLang=en&lang=sp&eventType=REG&after=2018-7-01&before=2018-8-18")).andReturn();
		
		verify(eventsService).processGet(timeRange.capture(), any(), any(), any());
		
		
		TimeRange timeRangeValue = timeRange.getValue();
		
		Assert.assertEquals(LocalDate.of(2018, 7, 1), timeRangeValue.getEventStartDate());
		Assert.assertEquals(LocalDate.of(2018, 8, 18), timeRangeValue.getEventEndDate());
		
		

	}
	
	/*
	 * SingleTranslatedEvents found
	 * 
	 * Body as expected, 200
	 */
	@Test
	public void givenValidEventsFound_thenGetAllEvents_responseAsExpected() throws Exception {
		List<SingleTranslatedEvent> stes = new ArrayList<>();
		
		SingleTranslatedEvent sampleEvent = new SingleTranslatedEvent();
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
		
		SingleTranslatedEvent sampleEvent2 = new SingleTranslatedEvent();
		sampleEvent2.setComments("comments-2");
		sampleEvent2.setCurrentTranslationId(5L);
		sampleEvent2.setDataLanguage("sp-2");
		sampleEvent2.setDefaultDataLang("sp-2");
		sampleEvent2.setEventEndDate(LocalDate.of(2018, 7, 8).plusDays(3));
		sampleEvent2.setEventStartDate(LocalDate.of(2018, 7, 6).minusDays(2));
		sampleEvent2.setEventLanguage("en-2");
		sampleEvent2.setEventTitle("Title-2");
		sampleEvent2.setEventType("OTHER");
		sampleEvent2.setId(54L);
		
		stes.add(sampleEvent);
		stes.add(sampleEvent2);
		
		doReturn(stes).when(eventsService).processGet(any(), any(), any(), any());
		
		mockMvc.perform(get("/events/translated?eventLang=en&lang=sp&eventType=REG&after=2018-07-01&before=2018-08-18")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$..events.length()").value(2));

	}
	
	/*
	 * No events found
	 * 
	 * 404
	 */
	@Test
	public void givenNoMatch_thenGetAllEvents_returns404() throws Exception {
		
		doReturn(new ArrayList<>()).when(eventsService).processGet(any(), any(), any(), any());
		
		mockMvc.perform(get("/events/translated?eventLang=en&lang=sp&eventType=REG&after=2018-07-01&before=2018-08-18")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}
	
	/*
	 * No events found
	 * 
	 * 404
	 */
	@Test
	public void givenNoMatchNull_thenGetAllEvents_returns404() throws Exception {
		
		doReturn(null).when(eventsService).processGet(any(), any(), any(), any());
		
		mockMvc.perform(get("/events/translated?eventLang=en&lang=sp&eventType=REG&after=2018-07-01&before=2018-08-18")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

	
}
