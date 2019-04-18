package com.qah.kiosk.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.service.EventsService;

@Lazy
@Component
public class EventValidatorUtility {
	
	private EventsService eventsService;
	
	@Autowired
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	/* 
	 * Does an event like this exist in the list that was provided?
	 * 
	 * The List<GenericEvent> should consist of events whose dates already match 
	 * 
	 * delegates to isEventDuplicate(...)
	 * 
	 * DOES NOT log errors in Errors object
	 * 
	 * returns true if a duplicate is found either in the event or in its translations
	 */
	public boolean checkForDuplicatesInList(String eventTitle, String eventLanguage, String dataLanguage, String eventType, List<GenericEvent> eventsOnSameDates) {

		
		if(eventsOnSameDates == null || eventsOnSameDates.size() < 1) {
			// no event on this date
			return false;
		}
		
		// there's an event on this date - make sure it isn't the same
		String prevEventType = null;
		
		for(GenericEvent previousEvent : eventsOnSameDates) {
			prevEventType = previousEvent.getEventType();
			
			if(prevEventType.equals(eventType)) {
				
				// same event type - so continue checking
				
				if(dataLanguage.equals(previousEvent.getDataLanguage())) {
					if(isEventDuplicate(eventTitle, eventLanguage, dataLanguage, previousEvent)) {
						// this event already exists
						
						return true;
					} else {
						// Since the dataLanguage is the same for both events but they differ 
						// in either eventTitle or eventLanguage...
						//    1. We assume that their corresponding
						//       translations will reflect those changes
						//    2. The translations should not match on dataLanguage because
						//       dataLanguage of the event should not be repeated as a tr.
						//
						// Translations will therefore not be checked.
						continue;
					}

				} else {
					// Check for possible match in translations
					// Ex: A previous GenericEvent, when using one of its translations, might
					//     match the current event. This is a duplicate.
					if(previousEvent.getEventTranslations() == null) {
						continue;
					}
					for(EventTranslationObject translation : previousEvent.getEventTranslations()) {
						if(isEventDuplicate(eventTitle, eventLanguage, dataLanguage, translation)) {
							// this event already exists
							return true;
						} else {
							// there can't be a match - dataLanguage must match for a a full match
							// continue to next translation
							continue;
						}

					}
					
					// all translations exhausted, none matched
					// continue
					
					
					
				}
				
				// continue
				
				
				
			} else {
				// different type of event - don't bother validating translations
				
				continue;
			}
			
		}
		
		
		// no match found
		return false;
		
		
	}
	
	
	public boolean hasDuplicatesExhaustiveCheck(GenericEvent event, List<GenericEvent> events) {
		
		String eventTitle = event.getEventTitle();
		String eventLanguage = event.getEventLanguage();
		String dataLanguage = event.getDataLanguage();
		String eventType = event.getEventType();
		
		
		// check for duplicates
		boolean duplicateFound = false;
		
		duplicateFound = checkForDuplicatesInList(eventTitle, eventLanguage, dataLanguage, eventType, events);
		
		if(duplicateFound) {
			return true;
		}
		
		List<EventTranslationObject> eventTranslations = event.getEventTranslations();
		
		if(eventTranslations == null || eventTranslations.size() < 1) {
			return false;
		}
		// check translations for duplicate
		for(EventTranslationObject eto : eventTranslations) {
			eventTitle = eto.getEventTitle();
			eventLanguage = eto.getEventLanguage();
			dataLanguage = eto.getDataLanguage();
			
			if(checkForDuplicatesInList(eventTitle, eventLanguage, dataLanguage, eventType, events)) {
				return true;
			} else {
				// continue to next translation
			}
		}
		
		// no duplicate found
		return false;
	}


	/*
	 * Returns true if provided translation is a duplicate
	 * 
	 * Checks:
	 * 	1. eventTitle
	 * 	2. eventLanguage
	 * 	3. dataLanguage
	 * 
	 * of translation for a match
	 * 
	 *  
	 */
	
	public boolean isEventDuplicate(String eventTitle, String eventLanguage, String dataLanguage, EventTranslationObject otherTranslation) {
	
		String otherEventTitle = otherTranslation.getEventTitle();;
		String otherEventLanguage = otherTranslation.getEventLanguage();
		String otherDataLanguage = otherTranslation.getDataLanguage();
		
		if(otherDataLanguage.equals(dataLanguage)) {			
				if(otherEventTitle.equals(eventTitle) && otherEventLanguage.equals(eventLanguage)){
					// this event already exists!!
					return true;
					
				} else {
					// new event
					
					return false;
				}
				
		}
		
		return false;
	
	}
	
	/*
	 * Checks for:
	 * 	1. No repeated languages in provided list
	 * 	2. No EventTranslation dataLanguage should match provided dataLanguage
	 * 
	 * If Errors object is provided, it logs errors in Errors object and always returns true
	 * Note: it expects the Errors object path to be pointing to an object that has a eventTranslations[] field
	 * 
	 */
	public boolean validateTranslationsNoDuplicate(List<EventTranslationObject> translations, String dataLanguage, Errors errors) {
		List<String> dataLanguages = new ArrayList<>();
		int counter = 0;
		
		if(translations == null) {
			return true;
		}
		
		String currentField = null;


		for(EventTranslationObject translation : translations) {

			currentField = "eventTranslations[" + counter + "]";
			if(dataLanguages.contains(translation.getDataLanguage())) {
				// duplicate language
				if(errors != null) {
					errors.rejectValue(currentField, "events.eventTranslation.duplicate", new String[]{dataLanguage}, null);
				} else {
					return false;
				}

				
			} else {
				dataLanguages.add(translation.getDataLanguage());
			}
			
			if(dataLanguage.equals(translation.getDataLanguage())) {
				if(errors != null) {
					errors.rejectValue(currentField, "object.duplicate", new String[]{"translation", "the event as the default translation"}, null);
				} else {
					return false;
				}

			}
			
			counter++;
		}
		
		return true;
		
	}
	
	/* 
	 * Does an event like this exist already in the database?
	 * 
	 * 1. Loads events on this day using eventsService
	 * 2. Delegates to hasDuplicatesExhaustiveCheck() to perform the actual check
	 * 
	 */
	public boolean validateEventDateWithRepo(GenericEvent event) {
		
		// get events on this day
		List<GenericEvent> eventsAlreadyOnThisDay = eventsService.getGenericEventsStrictlyOn(event.getEventStartDate(), event.getEventEndDate());
		
		return hasDuplicatesExhaustiveCheck(event, eventsAlreadyOnThisDay);
		
	}
	
	
	/*
	 * Checks for:
	 * 	1. No id can be provided
	 * 	2. validateTranslationInfo(...):
	 * 		1. dataLanguage is not null
	 * 		2. eventTitle is not null
	 * 
	 * 
	 */
	
	public void validateTranslationInfoNoId(EventTranslationObject translation, Errors errors) {
		
		if(translation == null) {
			return;
		}
		
		if(translation.getId() != null) {
			errors.rejectValue("id", "property.must_be_null", new String[]{"id"}, null);
		}
		
		validateTranslationInfo(translation, errors);
		
	}

	/*
	 * 
	 * Checks for:
	 * 	1. dataLanguage is not null
	 * 	2. eventTitle is not null
	 * 
	 */
	public void validateTranslationInfo(EventTranslationObject tr, Errors errors) {
		if(tr.getDataLanguage() == null || tr.getDataLanguage().isEmpty()) {
			errors.rejectValue("dataLanguage", "property.cannot_be__null", new String[]{"dataLanguage"}, null);
		}
		if(tr.getEventTitle() == null || tr.getEventTitle().isEmpty()) {
			errors.rejectValue("eventTitle", "property.cannot_be__null", new String[]{"eventTitle"}, null);
		}
		
	}
	
	public EventTranslationObject extractDefaultTranslation(GenericEvent event) {
		
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments(event.getComments());
		eto.setDataLanguage(event.getDataLanguage());
		eto.setEventLanguage(event.getEventLanguage());
		eto.setEventTitle(event.getEventTitle());
		eto.setId(event.getDefaultTranslationId());
		
		return eto;
	}
	
}
