package com.qah.kiosk.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.impl.jdbc.DefaultEventsRepository;
import com.qah.kiosk.service.utils.TimeRange;

public class DefaultEventsServiceTest {
	
	@Test
	public void whenNoEvents_thenProcessGet_returnsEmptyList() {
		DefaultEventsService service = new DefaultEventsService();
		
		List<Event> emptyList = Collections.emptyList();
		
		DefaultEventsRepository repo = mock(DefaultEventsRepository.class);
		
		doReturn(emptyList).when(repo).getEventsAfter(any(), any(), any());
		doReturn(emptyList).when(repo).getEventsAllDates(any(), any());
		doReturn(emptyList).when(repo).getEventsBefore(any(), any(), any());
		doReturn(emptyList).when(repo).getEventsBetween(any(), any(), any(), any());
		
		service.setEventsRepository(repo);
		
		Assert.assertTrue(service.processGet(TimeRange.createTimeRange(LocalDate.now(), LocalDate.now()), "test", null).size() == 0);
		
	}
	
	@Test
	public void whenNoEvents_thenProcessGetTranslated_returnsEmptyList() {
		DefaultEventsService service = new DefaultEventsService();
		
		List<Event> emptyList = Collections.emptyList();
		
		DefaultEventsRepository repo = mock(DefaultEventsRepository.class);
		
		doReturn(emptyList).when(repo).getEventsAfter(any(), any(), any());
		doReturn(emptyList).when(repo).getEventsAllDates(any(), any());
		doReturn(emptyList).when(repo).getEventsBefore(any(), any(), any());
		doReturn(emptyList).when(repo).getEventsBetween(any(), any(), any(), any());
		
		service.setEventsRepository(repo);
		
		Assert.assertTrue(service.processGet(TimeRange.createTimeRange(LocalDate.now(), LocalDate.now()), "test", null, null).size() == 0);
		
	}
	
	@Test
	public void whenEventsNoTranslation_thenProcessGetTranslated_returnsEmptyList() {
		DefaultEventsService service = new DefaultEventsService();
		
		Event ev1 = new Event();
		ev1.setDefaultDataLang("en");
		ev1.setEventEndDate(LocalDate.now());
		ev1.setEventStartDate(LocalDate.now());
		ev1.setEventTranslations(null); 
		ev1.setEventType("REG");
		ev1.setId(1L);
		
		Event ev2 = new Event();
		ev2.setDefaultDataLang("en");
		ev2.setEventEndDate(LocalDate.now());
		ev2.setEventStartDate(LocalDate.now());
		ev2.setEventTranslations(new ArrayList<>()); 
		ev2.setEventType("REG");
		ev2.setId(2L);
		
		EventTranslation et = new EventTranslation();
		et.setComments("Comentarios");
		et.setDataLanguage("en");
		et.setEventLanguage("en");
		et.setEventTitle("Title");
		et.setId(1L);
		
		List<EventTranslation> etos = new ArrayList<>();
		etos.add(et);
		
		Event ev3 = new Event();
		ev3.setDefaultDataLang("en");
		ev3.setEventEndDate(LocalDate.now());
		ev3.setEventStartDate(LocalDate.now());
		ev3.setEventTranslations(etos); 
		ev3.setEventType("REG");
		ev3.setId(3L);
		
		List<Event> eventList = new ArrayList<>();
		eventList.add(ev1);
		eventList.add(ev2);
		eventList.add(ev3);

		
		DefaultEventsRepository repo = mock(DefaultEventsRepository.class);
		
		doReturn(eventList).when(repo).getEventsAfter(any(), any(), any());
		doReturn(eventList).when(repo).getEventsAllDates(any(), any());
		doReturn(eventList).when(repo).getEventsBefore(any(), any(), any());
		doReturn(eventList).when(repo).getEventsBetween(any(), any(), any(), any());
		
		service.setEventsRepository(repo);
		
		Assert.assertTrue(service.processGet(TimeRange.createTimeRange(LocalDate.now(), LocalDate.now()), "test", null, "ko").size() == 0);
		
	}
	
	@Test
	public void ifNoRowsAffected_thenProcessDelete_returnsFalse() {
		DefaultEventsService service = new DefaultEventsService();
		
		List<Event> emptyList = Collections.emptyList();
		
		DefaultEventsRepository repo = mock(DefaultEventsRepository.class);

		doReturn(emptyList).when(repo).getEventsAfter(any(), any(), any());
		doReturn(emptyList).when(repo).getEventsAllDates(any(), any());
		doReturn(emptyList).when(repo).getEventsBefore(any(), any(), any());
		doReturn(emptyList).when(repo).getEventsBetween(any(), any(), any(), any());
		
		service.setEventsRepository(repo);
		
		Assert.assertFalse(service.processDelete(TimeRange.createTimeRange(LocalDate.now(), LocalDate.now()), "test", null));
		
	}
	
	@Test
	public void ifRowsAffected_thenProcessDelete_returnsTrue() {
		DefaultEventsService service = new DefaultEventsService();
		
		List<Event> responseList = new ArrayList<>();
		responseList.add(new Event());
		
		DefaultEventsRepository repo = mock(DefaultEventsRepository.class);

		doReturn(responseList).when(repo).getEventsAfter(any(), any(), any());
		doReturn(responseList).when(repo).getEventsAllDates(any(), any());
		doReturn(responseList).when(repo).getEventsBefore(any(), any(), any());
		doReturn(responseList).when(repo).getEventsBetween(any(), any(), any(), any());
		
		service.setEventsRepository(repo);
		
		Assert.assertTrue(service.processDelete(TimeRange.createTimeRange(LocalDate.now(), LocalDate.now().plusDays(54)), "test", null));
		
	}

}
