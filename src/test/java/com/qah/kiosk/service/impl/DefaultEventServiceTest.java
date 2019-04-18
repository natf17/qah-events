package com.qah.kiosk.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.EventRepository;
import com.qah.kiosk.service.TranslationsService;

public class DefaultEventServiceTest {
	
	@Test
	public void givenInvalidId_thenGetEvent_returnsNull() {
		DefaultEventService eventService = new DefaultEventService();
		
		Assert.assertNull(eventService.getEvent(-34L));
	}
	
	@Test
	public void givenNonexistantId_thenGetEvent_returnsNull() {
		DefaultEventService eventService = new DefaultEventService();
		
		EventRepository repo = mock(EventRepository.class);
		when(repo.getEvent(34L)).thenReturn(null);
		eventService.setEventRepository(repo);
		
		Assert.assertNull(eventService.getEvent(34L));
	}

	@Test
	public void givenInvalidId_thenDeleteEvent_returnsFalse() {
		DefaultEventService eventService = new DefaultEventService();
		EventRepository repo = mock(EventRepository.class);
		when(repo.deleteEvent(34L)).thenReturn(0);
		eventService.setEventRepository(repo);
		
		Assert.assertFalse(eventService.deleteEvent(34L));
	}
	
	@Test
	public void givenValidId_thenDeleteEvent_returnsFalse() {
		DefaultEventService eventService = new DefaultEventService();
		EventRepository repo = mock(EventRepository.class);
		when(repo.deleteEvent(34L)).thenReturn(1);
		eventService.setEventRepository(repo);
		
		Assert.assertTrue(eventService.deleteEvent(34L));
	}
	
	@Test
	public void givenNonexistantIdEvent_thenGetEnclosingEvent_returnsNull() {
		DefaultEventService eventService = new DefaultEventService();
		
		TranslationsService service = mock(TranslationsService.class);
		when(service.getParentEventId(34L)).thenReturn(null);
		eventService.setTranslationsService(service);
		
		Assert.assertNull(eventService.getEnclosingEvent(34L));
	}
	
	@Test
	public void givenNonexistantIdEvent_thenGetTranslatedEvent_returnsNull() {
		DefaultEventService eventService = new DefaultEventService();
		
		EventRepository repo = mock(EventRepository.class);
		when(repo.getEvent(34L)).thenReturn(null);
		eventService.setEventRepository(repo);
		
		Assert.assertNull(eventService.getTranslatedEvent(34L, "sp"));
	}
	
	@Test
	public void givenNonexistantTranslation_thenGetTranslatedEvent_returnsNull() {
		DefaultEventService eventService = new DefaultEventService();
		
		Event eve = new Event();
		eve.setDefaultDataLang("defaultDataLanguage");
		eve.setEventEndDate(LocalDate.of(2019, 3, 28));
		eve.setEventEndDate(LocalDate.of(2019, 3, 25));
		eve.setEventType("CA-CO");
		eve.setId(34L);

		EventTranslation et = new EventTranslation();
		et.setComments("Comments");
		et.setDataLanguage("sample");
		et.setEventLanguage("eventLanguage");
		et.setEventTitle("title");
		et.setId(5L);
		
		EventTranslation et2 = new EventTranslation();
		et2.setComments("Comentarios");
		et2.setDataLanguage("defaultDataLanguage");
		et2.setEventLanguage("eventLanguage2");
		et2.setEventTitle("title2");
		et2.setId(6L);

		
		eve.setEventTranslations(Arrays.asList(et, et2));
		
		EventRepository repo = mock(EventRepository.class);
		when(repo.getEvent(34L)).thenReturn(eve);
		eventService.setEventRepository(repo);
		
		Assert.assertNull(eventService.getTranslatedEvent(34L, "sample-other"));
	}
	
	@Test
	public void givenExistingranslation_thenGetTranslatedEvent_returnsNotNull() {
		DefaultEventService eventService = new DefaultEventService();
		
		Event eve = new Event();
		eve.setDefaultDataLang("defaultDataLanguage");
		eve.setEventEndDate(LocalDate.of(2019, 3, 28));
		eve.setEventEndDate(LocalDate.of(2019, 3, 25));
		eve.setEventType("CA-CO");
		eve.setId(34L);

		EventTranslation et = new EventTranslation();
		et.setComments("Comments");
		et.setDataLanguage("sample");
		et.setEventLanguage("eventLanguage");
		et.setEventTitle("title");
		et.setId(5L);
		
		EventTranslation et2 = new EventTranslation();
		et2.setComments("Comentarios");
		et2.setDataLanguage("defaultDataLanguage");
		et2.setEventLanguage("eventLanguage2");
		et2.setEventTitle("title2");
		et2.setId(6L);

		
		eve.setEventTranslations(Arrays.asList(et, et2));
		
		EventRepository repo = mock(EventRepository.class);
		when(repo.getEvent(34L)).thenReturn(eve);
		eventService.setEventRepository(repo);
		
		Assert.assertNotNull(eventService.getTranslatedEvent(34L, "sample"));
	}
	
	
}
