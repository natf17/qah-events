package com.qah.kiosk.validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.service.EventService;

@Lazy
@Component
public class GenericEventPutValidator implements Validator {

	private GenericEventPartialValidator eventTypePartialValidator;
	
	private EventValidatorUtility validatorUtility;
	
	private EventService eventService;
	

	@Autowired
	private void setGenericEventPartialValidator(GenericEventPartialValidator eventTypePartialValidator) {
		this.eventTypePartialValidator = eventTypePartialValidator;
	}
	
	@Autowired
	private void setEventValidatorUtility(EventValidatorUtility validatorUtility) {
		this.validatorUtility = validatorUtility;
	}
	
	@Autowired
	private void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return GenericEvent.class.isAssignableFrom(clazz);
	}

	/*
	 * Validation:
	 * 
	 * 1. GenericEventPartialValidator:
	 * 		1. eventType is not null
	 *  	2. eventType is valid
	 *  	3. eventTitle is not null
	 *  	4. eventLanguage is not null
	 *  	5. dataLanguage and defaultDataLanguage cannot both be null
	 *  	6. if both are provided, they must match
	 *  	7. startDate is not null
	 *  	8. if endDate is not provided, startDate is used
	 *  	9. if endDate is provided, it must be after startDate
	 *  
	 * 2. id of event must be provided
	 * 3. defaultTranslationId must be null
	 * 3. An event with that id must already exist
	 * 
	 * 5. EventValidatorUtility:
	 * 		1. No id in any EventTranslationObject
	 * 		2. Each EventTranslationObject must have valid information
	 * 		3. No repeated languages in translations
	 * 
	 * PUTing an event allows a duplicate event to be persisted.
	 * It will not check to see if this event will conflict with any that already exist
	 */
	@Override
	public void validate(Object event, Errors errors) {
		
		eventTypePartialValidator.validate(event, errors);
		
		if(errors.hasErrors()) {
			return;
		}
		
		GenericEvent newEvent = (GenericEvent)event;
		Long eventId = newEvent.getId();
		
		if(eventId == null) {
			errors.rejectValue("id", "property.cannot_be__null", new String[]{"id"}, null);
			return;
		}
		
		if(newEvent.getDefaultTranslationId() != null) {
			errors.rejectValue("defaultTranslationId", "property.must_be_null", new String[]{"id"}, null);
			
		}
		
		if(eventService.getEvent(eventId) == null) {
			errors.reject("object.must_exist", new String[]{"The event", "id", eventId.toString()}, null);
			
			return;
			
		}
		
		/*
		 * Validate the integrity of the translations...
		 * 
		 * No more than one translation language per event
		 * 
		 */
		List<EventTranslationObject> translations = newEvent.getEventTranslations();
		if(translations == null) {
			return;
		}
		
		int translationCount = 0;
		int errorCount = errors.getErrorCount();
		for(EventTranslationObject tr : translations) {
			errors.pushNestedPath("eventTranslations[" + translationCount + "]");
			validatorUtility.validateTranslationInfoNoId(tr, errors);
			errors.popNestedPath();
		}
		if(errors.getErrorCount() > errorCount) {
			return;
		}
		
		validatorUtility.validateTranslationsNoDuplicate(translations, newEvent.getDataLanguage(), errors);
		
	}

}
