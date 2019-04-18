package com.qah.kiosk.repository.impl.jdbc;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.EventRepository;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
public class DefaultTranslationRepositoryTest {
	
	@Autowired
	private DefaultTranslationRepository repo;
	
	@Autowired
	private EventRepository eventRepo;
	
	private Event parentEvent;
	
	// Save an empty event (without translations)
	@Before
	public void saveParentEvent() {
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CACO");
		
		// save sample event
		parentEvent = eventRepo.postEvent(ev);
	}

	
	// Use getTranslation() to test saveTranslation()
	@Test
	public void saveTranslationTest() {
		
		EventTranslation tr = new EventTranslation();
		tr.setComments("Comments");
		tr.setDataLanguage("es");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		EventTranslation trS = repo.saveTranslation(parentEvent.getId(), tr);

		EventTranslation trR = repo.getTranslation(trS.getId());
		
		Assert.assertTrue(trR.getId().equals(trS.getId()));
		
	}
	
	// First, saveTranslation() and then test getTranslation()
	@Test
	public void whenSavingEvent_thenGetEventTranslationsForEventId_returnsTranslations() {
	
		EventTranslation tr = new EventTranslation();
		
		tr.setComments("Comments");
		tr.setDataLanguage("es");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		EventTranslation tr2 = new EventTranslation();
		
		tr2.setComments("Comentarios");
		tr2.setDataLanguage("sp");
		tr2.setEventLanguage("en");
		tr2.setEventTitle("Titulo");
		
		Long parentEventId = parentEvent.getId();
		
		repo.saveTranslation(parentEventId, tr);
		repo.saveTranslation(parentEventId, tr2);
		
		Assert.assertEquals(2, repo.getEventTranslationsForEventId(parentEventId).size());
		
	}
	
	// First, saveTranslation() and then test getEnclosingEventId()
	@Test
	public void getEnclosingEventId_returnsCorrectId() {
		EventTranslation tr = new EventTranslation();
		
		tr.setComments("Comments");
		tr.setDataLanguage("es");
		tr.setEventLanguage("en");
		tr.setEventTitle("Title");
		
		Long parentEventId = parentEvent.getId();

		EventTranslation trS = repo.saveTranslation(parentEventId, tr);

		Assert.assertTrue(repo.getEnclosingEventId(trS.getId()).equals(parentEventId));
		
	}
	
	// When a translation doesn't exist, return null
	@Test
	public void givenNonExistingId_thenGetTranslation_returnsNull() {

		Assert.assertNull(repo.getTranslation(6543L));
		
	}
	
}
