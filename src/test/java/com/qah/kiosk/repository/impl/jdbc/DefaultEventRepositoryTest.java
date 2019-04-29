package com.qah.kiosk.repository.impl.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
public class DefaultEventRepositoryTest {
	
	@Autowired
	private DefaultEventRepository eventRepo;
	
	
	@Test
	public void whenPostEvent_thenGetReturnsEvent() {
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CACO");
		

		EventTranslation tr = new EventTranslation();
		
		tr.setComments("Comments");
		tr.setDataLanguage("en");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		EventTranslation tr2 = new EventTranslation();
		
		tr2.setComments("Comentarios");
		tr2.setDataLanguage("sp");
		tr2.setEventLanguage("en");
		tr2.setEventTitle("Titulo");
		
		List<EventTranslation> translations = new ArrayList<EventTranslation>();
		
		translations.add(tr);
		translations.add(tr2);
		ev.setEventTranslations(translations);
		
		// save sample event
		ev = eventRepo.postEvent(ev);
		Long id = ev.getId();
		
		// get event
		ev = eventRepo.getEvent(id);
		
		Assert.assertEquals(id, ev.getId());
		
	}
	/*
	@Test
	public void whenPostEvent_thenMethodReturnsEventWithTranslations() {
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CACO");
		

		EventTranslation tr = new EventTranslation();
		
		tr.setComments("Comments");
		tr.setDataLanguage("en");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		EventTranslation tr2 = new EventTranslation();
		
		tr2.setComments("Comentarios");
		tr2.setDataLanguage("sp");
		tr2.setEventLanguage("en");
		tr2.setEventTitle("Titulo");
		
		List<EventTranslation> translations = new ArrayList<EventTranslation>();
		
		translations.add(tr);
		translations.add(tr2);
		ev.setEventTranslations(translations);
		
		// save sample event
		ev = eventRepo.postEvent(ev);
		
		Assert.assertEquals(2, ev.getEventTranslations().size());
		
	}
	
	@Test
	public void whenPostEvent_thenGetReturnsEventWithTranslations() {
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CACO");
		

		EventTranslation tr = new EventTranslation();
		
		tr.setComments("Comments");
		tr.setDataLanguage("en");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		EventTranslation tr2 = new EventTranslation();
		
		tr2.setComments("Comentarios");
		tr2.setDataLanguage("sp");
		tr2.setEventLanguage("en");
		tr2.setEventTitle("Titulo");
		
		List<EventTranslation> translations = new ArrayList<EventTranslation>();
		
		translations.add(tr);
		translations.add(tr2);
		ev.setEventTranslations(translations);
		
		// save sample event
		ev = eventRepo.postEvent(ev);
		Long id = ev.getId();
		
		// get event
		ev = eventRepo.getEvent(id);
		
		Assert.assertEquals(2, ev.getEventTranslations().size());
		
	}
	
	@Test
	public void whenPutEventWithOneFewerTranslation_thenGetReturnsUpdatedEvent() {
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CACO");
		

		EventTranslation tr = new EventTranslation();
		
		tr.setComments("Comments");
		tr.setDataLanguage("en");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		EventTranslation tr2 = new EventTranslation();
		
		tr2.setComments("Comentarios");
		tr2.setDataLanguage("sp");
		tr2.setEventLanguage("en");
		tr2.setEventTitle("Titulo");
		
		List<EventTranslation> translations = new ArrayList<>();
		
		translations.add(tr);
		translations.add(tr2);
		ev.setEventTranslations(translations);
		
		// save sample event
		ev = eventRepo.postEvent(ev);
		Long id = ev.getId();
		
		translations = new ArrayList<>();
		translations.add(tr);
		
		ev.setEventTranslations(translations);
		
		// update the event
		ev = eventRepo.putEvent(ev);
		
		// get event
		ev = eventRepo.getEvent(id);
		
		Assert.assertEquals(1, ev.getEventTranslations().size());
		
	}
	
	@Test
	public void givenIdOfNonexistingEvent_thenGetEvent_returnsNull() {
		Assert.assertNull(eventRepo.getEvent(3029L));
	}
	
	@Test
	public void givenIdOfNonexistingEvent_thenDeleteEvent_returns0() {
		Assert.assertTrue(eventRepo.deleteEvent(3009L) == 0);
	}
	
	@Test
	public void givenIdOfEvent_thenDeleteEvent_returns1() {
		
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CACO");
		
		ev = eventRepo.postEvent(ev);

		
		Assert.assertTrue(eventRepo.deleteEvent(ev.getId()) == 1);
	}
*/
}
