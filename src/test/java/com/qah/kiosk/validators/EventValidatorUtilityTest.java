package com.qah.kiosk.validators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.service.EventsService;

public class EventValidatorUtilityTest {
	
	private EventValidatorUtility utility = new EventValidatorUtility();
	
	@Test
	public void givenNullDataLanguage_thenValidateTranslationInfo_hasFieldError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage(null);
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		utility.validateTranslationInfo(eto, errors);
		
		
		Assert.assertTrue(errors.hasFieldErrors("dataLanguage"));
	}
	
	@Test
	public void givenNullEventTitle_thenValidateTranslationInfo_hasFieldError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("en");
		eto.setEventLanguage("en");
		eto.setEventTitle(null);
		eto.setId(709L);
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		utility.validateTranslationInfo(eto, errors);
		
		
		Assert.assertTrue(errors.hasFieldErrors("eventTitle"));
	}
	
	@Test
	public void givenValidTranslation_thenValidateTranslationInfo_hasNoError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("en");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		utility.validateTranslationInfo(eto, errors);
		
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	@Test
	public void givenId_thenValidateTranslationInfoNoId_hasFieldError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("en");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		utility.validateTranslationInfoNoId(eto, errors);
		
		
		Assert.assertTrue(errors.hasFieldErrors("id"));
	}
	
	@Test
	public void givenDuplicateTranslation_thenValidateTranslationsNoDuplicate_returnsFalse() {
		EventTranslationObject eto1 = new EventTranslationObject();
		eto1.setComments("Comments");
		eto1.setDataLanguage("en");
		eto1.setEventLanguage("en");
		eto1.setEventTitle("Title");
		eto1.setId(709L);
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setComments("Comments");
		eto2.setDataLanguage("sp");
		eto2.setEventLanguage("en");
		eto2.setEventTitle("Title");
		eto2.setId(709L);
		
		EventTranslationObject eto3 = new EventTranslationObject();
		eto3.setComments("Comments");
		eto3.setDataLanguage("ko");
		eto3.setEventLanguage("en");
		eto3.setEventTitle("Title");
		eto3.setId(709L);
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto1);
		etos.add(eto2);
		etos.add(eto3);
		
		Assert.assertFalse(utility.validateTranslationsNoDuplicate(etos, "ko", null));
	}
	
	@Test
	public void givenValidTranslation_thenValidateTranslationsNoDuplicate_returnsTrue() {
		EventTranslationObject eto1 = new EventTranslationObject();
		eto1.setComments("Comments");
		eto1.setDataLanguage("en");
		eto1.setEventLanguage("en");
		eto1.setEventTitle("Title");
		eto1.setId(709L);
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setComments("Comments");
		eto2.setDataLanguage("sp");
		eto2.setEventLanguage("en");
		eto2.setEventTitle("Title");
		eto2.setId(709L);
		
		EventTranslationObject eto3 = new EventTranslationObject();
		eto3.setComments("Comments");
		eto3.setDataLanguage("ko");
		eto3.setEventLanguage("en");
		eto3.setEventTitle("Title");
		eto3.setId(709L);
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto1);
		etos.add(eto2);
		etos.add(eto3);
		
		Assert.assertTrue(utility.validateTranslationsNoDuplicate(etos, "pr", null));
	}
	
	@Test
	public void givenDuplicateTranslation_thenValidateTranslationsNoDuplicate_LogsError() {
		EventTranslationObject eto1 = new EventTranslationObject();
		eto1.setComments("Comments");
		eto1.setDataLanguage("en");
		eto1.setEventLanguage("en");
		eto1.setEventTitle("Title");
		eto1.setId(709L);
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setComments("Comments");
		eto2.setDataLanguage("sp");
		eto2.setEventLanguage("en");
		eto2.setEventTitle("Title");
		eto2.setId(709L);
		
		EventTranslationObject eto3 = new EventTranslationObject();
		eto3.setComments("Comments");
		eto3.setDataLanguage("ko");
		eto3.setEventLanguage("en");
		eto3.setEventTitle("Title");
		eto3.setId(709L);
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto1);
		etos.add(eto2);
		etos.add(eto3);
		
		GenericEvent eve = new GenericEvent();
		eve.setEventTranslations(etos);
		
		Errors errors = new BeanPropertyBindingResult(eve, "genericEvent");

		utility.validateTranslationsNoDuplicate(etos, "ko", errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTranslations[2]"));
	}
	
	@Test
	public void givenDuplicate_thenIsEventDuplicateReturnsTrue() {
		EventTranslationObject eto1 = new EventTranslationObject();
		eto1.setComments("Comments");
		eto1.setDataLanguage("en");
		eto1.setEventLanguage("en");
		eto1.setEventTitle("Title");
		eto1.setId(709L);
		
		Assert.assertTrue(utility.isEventDuplicate("Title", "en", "en", eto1));
	}
	
	@Test
	public void givenDifferentTitle_thenIsEventDuplicateReturnsFalse() {
		EventTranslationObject eto1 = new EventTranslationObject();
		eto1.setComments("Comments");
		eto1.setDataLanguage("en");
		eto1.setEventLanguage("en");
		eto1.setEventTitle("Title");
		eto1.setId(709L);
		
		Assert.assertFalse(utility.isEventDuplicate("Other", "en", "en", eto1));
	}
	
	@Test
	public void givenDifferentDataLanguage_thenIsEventDuplicateReturnsFalse() {
		EventTranslationObject eto1 = new EventTranslationObject();
		eto1.setComments("Comments");
		eto1.setDataLanguage("en");
		eto1.setEventLanguage("en");
		eto1.setEventTitle("Title");
		eto1.setId(709L);
		
		Assert.assertFalse(utility.isEventDuplicate("Title", "en", "other", eto1));
	}
	
	/*
	 * A GenericEvent that's exactly the same as one in the List<> is a duplicate
	 * 
	 * (GenericEvent refers to its properties, which are separate args)
	 */
	@Test
	public void givenSameEventAsExisting_thenCheckForDuplicatesInList_returnsTrue() {
		String eventTitle = "title";
		String eventLanguage = "en";
		String dataLanguage = "sp";
		String eventType = "CA-CO";
		List<GenericEvent> eventsOnSameDates = new ArrayList<>();
		
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setDefaultDataLang("es");
		ev2.setEventTranslations(new ArrayList<>());
		ev2.setComments("Comments");
		ev2.setDataLanguage("sp");
		ev2.setEventLanguage("en");
		ev2.setEventTitle("title");
		ev2.setDefaultTranslationId(709L);
		ev2.setEventType("CA-CO");
		
		eventsOnSameDates.add(ev1);
		eventsOnSameDates.add(ev2);
		
		Assert.assertTrue(utility.checkForDuplicatesInList(eventTitle, eventLanguage, dataLanguage, eventType, eventsOnSameDates));
		
	}
	
	/*
	 * A GenericEvent that exactly matches a translation of an event in the List<> is a duplicate
	 * 
	 * (GenericEvent refers to its properties, which are separate args)
	 */
	@Test
	public void givenSameAsTranslation_thenCheckForDuplicatesInList_returnsTrue() {
		String eventTitle = "title";
		String eventLanguage = "en";
		String dataLanguage = "sp";
		String eventType = "CA-CO";
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage(dataLanguage);
		eto.setEventLanguage(eventLanguage);
		eto.setEventTitle(eventTitle);
		
		List<GenericEvent> eventsOnSameDates = new ArrayList<>();
		
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setDefaultDataLang("es");
		ev2.setEventTranslations(new ArrayList<>());
		ev2.setComments("Comments");
		ev2.setDataLanguage("en");
		ev2.setEventLanguage("en");
		ev2.setEventTitle("Title");
		ev2.setDefaultTranslationId(709L);
		ev2.setEventType("CA-CO");
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev2.setEventTranslations(etos);
		
		eventsOnSameDates.add(ev1);
		eventsOnSameDates.add(ev2);
		
		Assert.assertTrue(utility.checkForDuplicatesInList(eventTitle, eventLanguage, dataLanguage, eventType, eventsOnSameDates));
		
	}
	
	/*
	 * A GenericEvent that only partially matches a translation of an event in the List<> is not a duplicate
	 * 
	 * (GenericEvent refers to its properties, which are separate args)
	 */
	@Test
	public void givenDifferentTranslation_thenCheckForDuplicatesInList_returnsFalse() {
		String eventTitle = "title";
		String eventLanguage = "en";
		String dataLanguage = "sp";
		String eventType = "CA-CO";
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage(dataLanguage);
		eto.setEventLanguage(eventLanguage);
		eto.setEventTitle(eventTitle);
		
		List<GenericEvent> eventsOnSameDates = new ArrayList<>();
		
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setDefaultDataLang("es");
		ev2.setEventTranslations(new ArrayList<>());
		ev2.setComments("Comments");
		ev2.setDataLanguage("en");
		ev2.setEventLanguage("en");
		ev2.setEventTitle("Title");
		ev2.setDefaultTranslationId(709L);
		ev2.setEventType("CA-CO");
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev2.setEventTranslations(etos);
		
		eventsOnSameDates.add(ev1);
		eventsOnSameDates.add(ev2);
		
		Assert.assertFalse(utility.checkForDuplicatesInList(eventTitle, eventLanguage, dataLanguage + "other", eventType, eventsOnSameDates));
		
	}
	

	/*
	 * A GenericEvent that only partially matches (same language but different title) an event in the List<> is not a duplicate
	 * 
	 * (GenericEvent refers to its properties, which are separate args)
	 */
	@Test
	public void givenSlightlyDifferentTranslation_thenCheckForDuplicatesInList_returnsFalse() {
		String eventTitle = "title";
		String eventLanguage = "en";
		String dataLanguage = "sp";
		String eventType = "CA-CO";
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage(dataLanguage);
		eto.setEventLanguage(eventLanguage);
		eto.setEventTitle(eventTitle);
		
		List<GenericEvent> eventsOnSameDates = new ArrayList<>();
		
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		
		GenericEvent ev2 = new GenericEvent();
		ev2.setDefaultDataLang("es");
		ev2.setEventTranslations(new ArrayList<>());
		ev2.setComments("Comments");
		ev2.setDataLanguage("en");
		ev2.setEventLanguage("en");
		ev2.setEventTitle("Title");
		ev2.setDefaultTranslationId(709L);
		ev2.setEventType("CA-CO");
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev2.setEventTranslations(etos);
		
		eventsOnSameDates.add(ev1);
		eventsOnSameDates.add(ev2);
		
		Assert.assertFalse(utility.checkForDuplicatesInList(eventTitle + "other", eventLanguage, dataLanguage, eventType, eventsOnSameDates));
		
	}
	
	/*
	 * A GenericEvent that exactly matches an event in the List is a duplicate
	 * 
	 */
	@Test
	public void givenDuplicateEvent_thenHasDuplicatesExhaustiveCheck_returnsTrue() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		ev1.setEventStartDate(eventStartDate);
		ev1.setEventEndDate(eventEndDate);
		
		List<GenericEvent> matchingEvents = new ArrayList<>();
		matchingEvents.add(ev1);
		
		EventValidatorUtility utility = new EventValidatorUtility();

		Assert.assertTrue(utility.hasDuplicatesExhaustiveCheck(ev1, matchingEvents));
		
	}
	
	
	
	/*
	 * A GenericEvent that exactly matches a translation of an event in the List is a duplicate
	 * 
	 */
	@Test
	public void givenEventThatExistsInTranslationOfEvent_thenHasDuplicatesExhaustiveCheck_returnsTrue() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		ev1.setEventStartDate(eventStartDate);
		ev1.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("es");
		eto.setEventLanguage("ing");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev1.setEventTranslations(etos);
		
		
		List<GenericEvent> matchingEvents = new ArrayList<>();
		matchingEvents.add(ev1);
		
		GenericEvent newEvent = new GenericEvent();
		newEvent.setDefaultDataLang("es");
		newEvent.setEventTranslations(new ArrayList<>());
		newEvent.setComments("Comments");
		newEvent.setDataLanguage("es");
		newEvent.setEventLanguage("ing");
		newEvent.setEventTitle("other");
		newEvent.setEventType("CA-BR");
		newEvent.setEventStartDate(eventStartDate);
		newEvent.setEventEndDate(eventEndDate);
		
		EventValidatorUtility utility = new EventValidatorUtility();
	
		
		Assert.assertTrue(utility.hasDuplicatesExhaustiveCheck(newEvent, matchingEvents));
		
	}
	
	
	/*
	 * A GenericEvent with a translation that exactly matches an event in the repository is a duplicate
	 * 
	 */
	@Test
	public void givenEventWithTranslationThatExistsAsEvent_thenHasDuplicatesExhaustiveCheck_returnsTrue() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		
		GenericEvent newEvent = new GenericEvent();
		newEvent.setDefaultDataLang("en");
		newEvent.setEventTranslations(new ArrayList<>());
		newEvent.setComments("Comments");
		newEvent.setDataLanguage("en");
		newEvent.setEventLanguage("en");
		newEvent.setEventTitle("Title");
		newEvent.setDefaultTranslationId(709L);
		newEvent.setEventType("CA-BR");
		newEvent.setEventStartDate(eventStartDate);
		newEvent.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		newEvent.setEventTranslations(etos);

		
		GenericEvent repoEvent = new GenericEvent();
		repoEvent.setDefaultDataLang("es");
		repoEvent.setEventTranslations(new ArrayList<>());
		repoEvent.setComments("Comments");
		repoEvent.setDataLanguage("es");
		repoEvent.setEventLanguage("en");
		repoEvent.setEventTitle("other");
		repoEvent.setEventType("CA-BR");
		repoEvent.setEventStartDate(eventStartDate);
		repoEvent.setEventEndDate(eventEndDate);
		
		List<GenericEvent> matchingEvents = new ArrayList<>();
		matchingEvents.add(repoEvent);
		
		EventValidatorUtility utility = new EventValidatorUtility();
		
		Assert.assertTrue(utility.hasDuplicatesExhaustiveCheck(newEvent, matchingEvents));
		
	}
	
												
	/* 
	 * A GenericEvent with a translation that exactly matches an event with a translation in the List is a duplicate
	 * 
	 */
	@Test
	public void givenEventWithTranslationThatExistsAsEventWithSameTranslation_thenHasDuplicatesExhaustiveCheck_returnsTrue() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		
		GenericEvent newEvent = new GenericEvent();
		newEvent.setDefaultDataLang("en");
		newEvent.setEventTranslations(new ArrayList<>());
		newEvent.setComments("Comments");
		newEvent.setDataLanguage("en");
		newEvent.setEventLanguage("en");
		newEvent.setEventTitle("Title");
		newEvent.setDefaultTranslationId(709L);
		newEvent.setEventType("CA-BR");
		newEvent.setEventStartDate(eventStartDate);
		newEvent.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		newEvent.setEventTranslations(etos);

		
		GenericEvent repoEvent = new GenericEvent();
		repoEvent.setDefaultDataLang("es");
		repoEvent.setEventTranslations(new ArrayList<>());
		repoEvent.setComments("Comments");
		repoEvent.setDataLanguage("es");
		repoEvent.setEventLanguage("en");
		repoEvent.setEventTitle("other");
		repoEvent.setEventType("CA-BR");
		repoEvent.setEventStartDate(eventStartDate);
		repoEvent.setEventEndDate(eventEndDate);
		
		repoEvent.setEventTranslations(etos);
		
		List<GenericEvent> matchingEvents = new ArrayList<>();
		matchingEvents.add(repoEvent);
		
		EventValidatorUtility utility = new EventValidatorUtility();
		
		Assert.assertTrue(utility.hasDuplicatesExhaustiveCheck(newEvent, matchingEvents));
		
	}
	
	/*
	 * If no events in the List, returns false
	 * 
	 */
	@Test
	public void givenNoEventsRepo_thenHasDuplicatesExhaustiveCheck_returnsFalse() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		
		GenericEvent newEvent = new GenericEvent();
		newEvent.setDefaultDataLang("en");
		newEvent.setEventTranslations(new ArrayList<>());
		newEvent.setComments("Comments");
		newEvent.setDataLanguage("en");
		newEvent.setEventLanguage("en");
		newEvent.setEventTitle("Title");
		newEvent.setDefaultTranslationId(709L);
		newEvent.setEventType("CA-BR");
		newEvent.setEventStartDate(eventStartDate);
		newEvent.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		newEvent.setEventTranslations(etos);

		
		EventValidatorUtility utility = new EventValidatorUtility();
		
		Assert.assertFalse(utility.hasDuplicatesExhaustiveCheck(newEvent, null));
		
	}
	
	/*
	 * If no events in the List, returns false
	 * 
	 * Test with an event without translations
	 * 
	 */
	@Test
	public void givenNoEventsRepoAndEventWithoutTranslations_thenHasDuplicatesExhaustiveCheck_returnsFalse() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		
		GenericEvent newEvent = new GenericEvent();
		newEvent.setDefaultDataLang("en");
		newEvent.setEventTranslations(new ArrayList<>());
		newEvent.setComments("Comments");
		newEvent.setDataLanguage("en");
		newEvent.setEventLanguage("en");
		newEvent.setEventTitle("Title");
		newEvent.setDefaultTranslationId(709L);
		newEvent.setEventType("CA-BR");
		newEvent.setEventStartDate(eventStartDate);
		newEvent.setEventEndDate(eventEndDate);

		
		EventValidatorUtility utility = new EventValidatorUtility();
		
		Assert.assertFalse(utility.hasDuplicatesExhaustiveCheck(newEvent, null));

	}
	
	/*
	 * A GenericEvent that exactly matches a translation of an event in the repository is a duplicate
	 * 
	 * For an exhaustive test of validateEventDateWithRepo(), see hasDuplicatesExhaustiveCheck() tests
	 * 
	 */
	@Test
	public void givenEventThatExistsInTranslationOfEvent_thenValidateEventDateWithRepo_returnsTrue() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		ev1.setEventStartDate(eventStartDate);
		ev1.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("es");
		eto.setEventLanguage("ing");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev1.setEventTranslations(etos);
		
		
		List<GenericEvent> matchingEvents = new ArrayList<>();
		matchingEvents.add(ev1);
		
		GenericEvent newEvent = new GenericEvent();
		newEvent.setDefaultDataLang("es");
		newEvent.setEventTranslations(new ArrayList<>());
		newEvent.setComments("Comments");
		newEvent.setDataLanguage("es");
		newEvent.setEventLanguage("ing");
		newEvent.setEventTitle("other");
		newEvent.setEventType("CA-BR");
		newEvent.setEventStartDate(eventStartDate);
		newEvent.setEventEndDate(eventEndDate);
		
		EventValidatorUtility utility = new EventValidatorUtility();
		EventsService eventsService = mock(EventsService.class);
		when(eventsService.getGenericEventsStrictlyOn(eventStartDate, eventEndDate)).thenReturn(matchingEvents);
		
		utility.setEventsService(eventsService);
		
		Assert.assertTrue(utility.validateEventDateWithRepo(newEvent));
		
	}

}
