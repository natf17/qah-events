package com.qah.kiosk.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.service.EventService;
import com.qah.kiosk.service.TranslationsService;

@Lazy
@Component
public class EventTranslationValidator implements Validator {
	
	private EventService eventService;
	
	private TranslationsService translationService;
	
	private EventValidatorUtility eventUtility;
	
	
	
	
	@Autowired
	private void setEventValidatorUtility(EventValidatorUtility validatorUtility) {
		this.eventUtility = validatorUtility;
	}
	
	@Autowired
	private void setTranslationsService(TranslationsService translationService) {
		this.translationService = translationService;
	}
	
	@Autowired
	private void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
	
	
	

	@Override
	public boolean supports(Class<?> clazz) {
		return EventTranslationObject.class.isAssignableFrom(clazz);
	}

	/*
	 * Used for PUT
	 * 
	 * Check for:
	 * 
	 * 1. id not null
	 * 2. EventTranslationObject with that id must exist
	 * 3. Cannot change the dataLanguage if the existing object's language was the defaultDataLanguage
	 * 4. eventUtility.validateTranslationInfo()
	 * 5. eventUtility.validateTranslationsNoDuplicate(): Make sure there would be no duplicate
	 */
	@Override
	public void validate(Object target, Errors errors) {
		EventTranslationObject tr = (EventTranslationObject)target;

		Long trId = tr.getId();
		String dataLanguage = tr.getDataLanguage();
		int errorCount = 0;
		
		if(trId == null) {
			errors.rejectValue("id", "property.cannot_be__null", new String[]{"id"}, null);

			return;
		}
		
		EventTranslationObject existingTranslation = translationService.getTranslation(trId);

		if(existingTranslation == null) {
			errors.rejectValue("id", "object.must_exist", new String[]{"A translation", "id", Long.toString(trId)}, null);
			return;
		}
		
		GenericEvent event = eventService.getEnclosingEvent(existingTranslation.getId());

		List<EventTranslationObject> existingTranslations = event.getEventTranslations();
		// extract the default translation from the GenericEvent to add
		existingTranslations.add(eventUtility.extractDefaultTranslation(event));
		
		existingTranslations.removeIf(i -> {
			// remove the translation this one is replacing
			return i.getId().equals(trId);
		});
		
		boolean defaultDataLangTr = false;
		if(! tr.getDataLanguage().equals(event.getDefaultDataLang())) {
			// MAKE SURE EVENT STILL HAS VALID defaultdatalanguage
			defaultDataLangTr = existingTranslations.stream().anyMatch(i -> i.getDataLanguage().equals(event.getDefaultDataLang()));
			
			if(!defaultDataLangTr) {
				errors.rejectValue("dataLanguage", "translation.put_leaves_event_without_defaultDataLang_translation");
			}
		}
		
		errorCount = errors.getErrorCount();

		
		eventUtility.validateTranslationInfo(tr, errors);

		if(errorCount == errors.getErrorCount()) {
			// make sure there is no translation that matches this one
			if(!eventUtility.validateTranslationsNoDuplicate(existingTranslations, dataLanguage, null)) {
				
				// duplicate language
				errors.reject("object.duplicate", new String[]{"translation", "repository"}, null);
				
			}
		}
				
		
		
	}
	
	/*
	 * Used for POST
	 * 
	 * Check for:
	 * 
	 * 1. an eventId for an existing event must be provided
	 * 2. id must be null
	 * 3. Cannot change the dataLanguage if the existing object's language was the defaultDataLanguage
	 * 4. eventUtility.validateTranslationInfo()
	 * 5. eventUtility.validateTranslationsNoDuplicate(): Make sure there would be no duplicate
	 */
	public void validate(Object target, Long eventId, Errors errors) {
		// make sure the event exists
		if(eventId == null) {
			errors.reject("translation.parent_event.does_not_exist", new String[]{"null"}, null);
			return;
		}
		
		if(eventId < 0) {
			errors.reject("translation.parent_event.does_not_exist", new String[]{Long.toString(eventId)}, null);
			return;
		}
		
		GenericEvent event = eventService.getEvent(eventId);
		
		if(event == null) {
			errors.reject("translation.parent_event.does_not_exist", new String[]{Long.toString(eventId)}, null);
			return;
		}
		
		// make sure it has no id
		EventTranslationObject tr = (EventTranslationObject)target;
		List<EventTranslationObject> translations = event.getEventTranslations();
		String dataLanguage = tr.getDataLanguage();
		int errorCount = 0;
		
		if(tr.getId() != null) {
			errors.rejectValue("id", "property.must_be_null", new String[]{"id"}, null);
		}
		
		errorCount = errors.getErrorCount();

		eventUtility.validateTranslationInfo(tr, errors);

		if(errorCount == errors.getErrorCount()) {
			if(translations == null) {
				translations = new ArrayList<>();
			}
			
			translations.add(eventUtility.extractDefaultTranslation(event));
			// make sure there is no translation that matches this one
			if(!eventUtility.validateTranslationsNoDuplicate(translations, dataLanguage, null)) {
			
				// duplicate language
				errors.reject("object.duplicate", new String[]{"translation", "repository"}, null);

			
			}
		}
		
	}

}
