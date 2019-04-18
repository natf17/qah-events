package com.qah.kiosk.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.util.GenericEventListWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
public class EventListValidatorTest {

	@Autowired
	private EventListValidator validator;
	
	private GenericEvent testEvent1;
	private GenericEvent testEvent2;
	private GenericEvent testEvent3;

	@Before
	public void setTestEvents() {
		testEvent1 = new GenericEvent();
		testEvent1.setEventType("REG");
		testEvent1.setComments("These are generic comments 1");
		testEvent1.setDataLanguage("en");
		testEvent1.setDefaultDataLang("en");
		testEvent1.setEventLanguage("sp");
		testEvent1.setEventEndDate(LocalDate.of(2019, 3, 30));
		testEvent1.setEventStartDate(LocalDate.of(2019, 3, 28));
		testEvent1.setEventTitle("Title1");
		testEvent1.setEventTranslations(new ArrayList<>());
		
		
		testEvent2 = new GenericEvent();
		testEvent2.setEventType("REG");
		testEvent2.setComments("These are generic comments 2");
		testEvent2.setDataLanguage("es");
		testEvent2.setDefaultDataLang("en");
		testEvent2.setEventLanguage("sp");
		testEvent2.setEventEndDate(LocalDate.of(2019, 3, 30));
		testEvent2.setEventStartDate(LocalDate.of(2019, 3, 29));
		testEvent2.setEventTitle("Title1");
		testEvent2.setEventTranslations(new ArrayList<>());
		
		
		testEvent3 = new GenericEvent();
		testEvent3.setEventType("REG");
		testEvent3.setComments("These are generic comments 3");
		testEvent3.setDataLanguage("es");
		testEvent3.setDefaultDataLang("en");
		testEvent3.setEventLanguage("sp");
		testEvent3.setEventEndDate(LocalDate.of(2019, 3, 30));
		testEvent3.setEventStartDate(LocalDate.of(2019, 3, 28));
		testEvent3.setEventTitle("Title3");
		testEvent3.setEventTranslations(new ArrayList<>());

		
	}

	/*
	 * GenericEvents with id result in errors.
	 */
	//@Test
	public void givenEventsWithId_thenValidate_forEach_logsFieldErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();
		
		List<GenericEvent> events = new ArrayList<>();
		
		testEvent1.setId(3L);
		testEvent2.setId(4L);
		
		events.add(testEvent1);
		events.add(testEvent2);
		
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertTrue(errors.hasFieldErrors("events[0].id"));
		assertTrue(errors.hasFieldErrors("events[1].id"));
		
	}
	
	/*
	 * GenericEvents with defaultTranslationId result in errors.
	 */
	//@Test
	public void givenEventsWithDefaultTranslationId_thenValidate_forEach_logsFieldErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();
		
		List<GenericEvent> events = new ArrayList<>();
		
		testEvent1.setDefaultTranslationId(3L);
		testEvent2.setDefaultTranslationId(4L);
		
		events.add(testEvent1);
		events.add(testEvent2);
		
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertTrue(errors.hasFieldErrors("events[0].defaultTranslationId"));
		assertTrue(errors.hasFieldErrors("events[1].defaultTranslationId"));
		
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * THE FOLLOWING TESTS ALTHOUGH REDUNDANT (they test hasDuplicatesExhaustiveTest() in 
	 * EventValidatorUtility), MAKE SURE REQUIREMENTS FOR validate()
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	/*
	 * Identical GenericEvents are duplicates.
	 */
	@Test
	public void givenDuplicateEventsInList_thenValidate_logsFieldErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();		
		
		GenericEvent dupEvent = new GenericEvent();
		dupEvent.setEventType(testEvent1.getEventType());
		dupEvent.setComments(testEvent1.getComments());
		dupEvent.setDataLanguage(testEvent1.getDataLanguage());
		dupEvent.setDefaultDataLang(testEvent1.getDefaultDataLang());
		dupEvent.setEventEndDate(testEvent1.getEventEndDate());
		dupEvent.setEventStartDate(testEvent1.getEventStartDate());
		dupEvent.setEventTitle(testEvent1.getEventTitle());
		dupEvent.setEventLanguage(testEvent1.getEventLanguage());
		dupEvent.setEventTranslations(new ArrayList<>());
		
		List<GenericEvent> events = new ArrayList<>();
		
		events.add(testEvent1);
		events.add(dupEvent);
				
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertTrue(errors.hasFieldErrors("events[1]"));
		
	}
	
	/*
	 * A GenericEvent that matches a GenericEvent's translation is a duplicate.
	 */
	@Test
	public void givenDuplicateEventsMatchesTranslationInList_thenValidate_logsFieldErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();		
		
		List<GenericEvent> events = new ArrayList<>();
		
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments(testEvent1.getComments() + "-other");
		eto.setDataLanguage(testEvent1.getDataLanguage() + "-other");
		eto.setEventLanguage(testEvent1.getEventLanguage() + "-other");
		eto.setEventTitle(testEvent1.getEventTitle() + "-other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		testEvent1.setEventTranslations(etos);
		
		events.add(testEvent1);
		
		
		GenericEvent dupEvent = new GenericEvent();
		dupEvent.setEventType(testEvent1.getEventType());
		dupEvent.setComments(eto.getComments());
		dupEvent.setDataLanguage(eto.getDataLanguage());
		dupEvent.setDefaultDataLang(eto.getDataLanguage());
		dupEvent.setEventEndDate(testEvent1.getEventEndDate());
		dupEvent.setEventStartDate(testEvent1.getEventStartDate());
		dupEvent.setEventTitle(eto.getEventTitle());
		dupEvent.setEventLanguage(eto.getEventLanguage());
		dupEvent.setEventTranslations(new ArrayList<>());
		
		events.add(dupEvent);
				
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertTrue(errors.hasFieldErrors("events[1]"));
		
	}
	
	/*
	 * A GenericEvent's translation that matches a GenericEvent's translation is a duplicate.
	 */
	@Test
	public void givenDuplicateEventsTranslationMatchesTranslationInList_thenValidate_logsFieldErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();		
		
		List<GenericEvent> events = new ArrayList<>();
		
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments(testEvent1.getComments() + "-other");
		eto.setDataLanguage(testEvent1.getDataLanguage() + "-other");
		eto.setEventLanguage(testEvent1.getEventLanguage() + "-other");
		eto.setEventTitle(testEvent1.getEventTitle() + "-other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		testEvent1.setEventTranslations(etos);
		
		events.add(testEvent1);
		
		
		GenericEvent dupEvent = new GenericEvent();
		dupEvent.setEventType(testEvent1.getEventType());
		dupEvent.setComments(testEvent1.getComments() + "-other2");
		dupEvent.setDataLanguage(testEvent1.getDataLanguage() + "-other2");
		dupEvent.setDefaultDataLang(testEvent1.getDataLanguage() + "-other2");
		dupEvent.setEventEndDate(testEvent1.getEventEndDate());
		dupEvent.setEventStartDate(testEvent1.getEventStartDate());
		dupEvent.setEventTitle(testEvent1.getEventTitle() + "-other2");
		dupEvent.setEventLanguage(testEvent1.getEventLanguage() + "-other2");
		
		dupEvent.setEventTranslations(etos);

		events.add(dupEvent);
				
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertTrue(errors.hasFieldErrors("events[1]"));
		
	}
	/*
	 * A GenericEvent with translations that matches GenericEvent with translation is a duplicate
	 * (default matches the default)
	 */
	@Test
	public void givenDuplicateEventWithTranslationMatchesEventWithTranslation_thenValidate_logsFieldErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();		
		
		List<GenericEvent> events = new ArrayList<>();
		
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments(testEvent1.getComments() + "-other");
		eto.setDataLanguage(testEvent1.getDataLanguage() + "-other");
		eto.setEventLanguage(testEvent1.getEventLanguage() + "-other");
		eto.setEventTitle(testEvent1.getEventTitle() + "-other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		testEvent1.setEventTranslations(etos);
		
		events.add(testEvent1);
		
		
		GenericEvent dupEvent = new GenericEvent();
		dupEvent.setEventType(testEvent1.getEventType());
		dupEvent.setComments(testEvent1.getComments());
		dupEvent.setDataLanguage(testEvent1.getDataLanguage());
		dupEvent.setDefaultDataLang(testEvent1.getDataLanguage());
		dupEvent.setEventEndDate(testEvent1.getEventEndDate());
		dupEvent.setEventStartDate(testEvent1.getEventStartDate());
		dupEvent.setEventTitle(testEvent1.getEventTitle());
		dupEvent.setEventLanguage(testEvent1.getEventLanguage());
		
		EventTranslationObject etoDup = new EventTranslationObject();
		etoDup.setComments(eto.getComments() + "-other");
		etoDup.setDataLanguage(eto.getDataLanguage() + "-other");
		etoDup.setEventLanguage(eto.getEventLanguage() + "-other");
		etoDup.setEventTitle(eto.getEventTitle() + "-other");
		
		List<EventTranslationObject> etosDup = new ArrayList<>();
		etosDup.add(etoDup);
		testEvent1.setEventTranslations(etosDup);

		events.add(dupEvent);
		
		
				
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertTrue(errors.hasFieldErrors("events[1]"));
		
	}
	
	/*
	 * Identical GenericEvents on different dates are not duplicates.
	 */
	@Test
	public void givenSameEventsOnDifferentDay_thenValidate_logsNoErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();		
		
		GenericEvent dupEvent = new GenericEvent();
		dupEvent.setEventType(testEvent1.getEventType());
		dupEvent.setComments(testEvent1.getComments());
		dupEvent.setDataLanguage(testEvent1.getDataLanguage());
		dupEvent.setDefaultDataLang(testEvent1.getDefaultDataLang());
		dupEvent.setEventEndDate(testEvent1.getEventEndDate());
		dupEvent.setEventStartDate(testEvent1.getEventStartDate().minusDays(2));
		dupEvent.setEventTitle(testEvent1.getEventTitle());
		dupEvent.setEventLanguage(testEvent1.getEventLanguage());
		dupEvent.setEventTranslations(new ArrayList<>());
		
		List<GenericEvent> events = new ArrayList<>();
		
		events.add(testEvent1);
		events.add(dupEvent);
				
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);

		assertFalse(errors.hasFieldErrors("events[1]"));
		
	}
	
	/*
	 * Different GenericEvents on same date are not duplicates.
	 * (eventType differs)
	 */
	@Test
	public void givenDifferentEventsTranslationOnSameDay_thenValidate_logsNoErrors() {
		GenericEventListWrapper wrap = new GenericEventListWrapper();		
		
		List<GenericEvent> events = new ArrayList<>();
		
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments(testEvent1.getComments() + "-other");
		eto.setDataLanguage(testEvent1.getDataLanguage() + "-other");
		eto.setEventLanguage(testEvent1.getEventLanguage() + "-other");
		eto.setEventTitle(testEvent1.getEventTitle() + "-other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		testEvent1.setEventTranslations(etos);
		
		events.add(testEvent1);
		
		
		GenericEvent otherEvent = new GenericEvent();
		otherEvent.setEventType(testEvent1.getEventType());
		otherEvent.setComments(testEvent1.getComments() + "-other2");
		otherEvent.setDataLanguage(testEvent1.getDataLanguage() + "-other2");
		otherEvent.setDefaultDataLang(testEvent1.getDataLanguage() + "-other2");
		otherEvent.setEventEndDate(testEvent1.getEventEndDate());
		otherEvent.setEventStartDate(testEvent1.getEventStartDate());
		otherEvent.setEventTitle(testEvent1.getEventTitle() + "-other2");
		otherEvent.setEventLanguage(testEvent1.getEventLanguage() + "-other2");
		
		EventTranslationObject etoOther = new EventTranslationObject();
		etoOther.setComments(eto.getComments() + "-other");
		etoOther.setDataLanguage(eto.getDataLanguage() + "-other");
		etoOther.setEventLanguage(eto.getEventLanguage() + "-other");
		etoOther.setEventTitle(eto.getEventTitle() + "-other");
		
		List<EventTranslationObject> etosOther = new ArrayList<>();
		etosOther.add(etoOther);
		testEvent1.setEventTranslations(etosOther);

		events.add(otherEvent);
				
		wrap.setEvents(events);
		
		Errors errors = new BeanPropertyBindingResult(wrap, "wrapper");
		
		validator.validate(wrap, errors);
		
		assertFalse(errors.hasFieldErrors("events[1]"));
		
	}
	
	/*
	 * For tests of translations, see EventValidatorUtilityTest
	 */

	
}
