package com.qah.kiosk.validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;

@Lazy
@Component
public class GenericEventFullValidator implements Validator {

	private GenericEventPartialValidator eventTypePartialValidator;
	
	private EventValidatorUtility validatorUtility;

	
	@Autowired
	private void setGenericEventPartialValidator(GenericEventPartialValidator eventTypePartialValidator) {
		this.eventTypePartialValidator = eventTypePartialValidator;
	}
	
	@Autowired
	private void setEventValidatorUtility(EventValidatorUtility validatorUtility) {
		this.validatorUtility = validatorUtility;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return GenericEvent.class.isAssignableFrom(clazz);
	}

	/*
	 * Validation:
	 * 
	 * 1.GenericEventPartialValidator:
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
	 * 2. id must be null
	 * 3. defaultTranslationId must be null
	 * 3. EventValidatorUtility:
	 * 		1. No duplicates with List<GenericEvent> in repo
	 * 		2. No id in any translation, must have valid information
	 * 		3. No repeated languages in translations
	 * 
	 */
	@Override
	public void validate(Object event, Errors errors) {
		eventTypePartialValidator.validate(event, errors);
		
		if(errors.hasErrors()) {
			return;
		}
		
		GenericEvent newEvent = (GenericEvent)event;
		
		if(newEvent.getId() != null) {
			errors.rejectValue("id", "property.must_be_null", new String[]{"id"}, null);
		}
		
		if(newEvent.getDefaultTranslationId() != null) {
			errors.rejectValue("defaultTranslationId", "property.must_be_null", new String[]{"defaultTranslationId"}, null);
			
		}
		
		if(validatorUtility.validateEventDateWithRepo(newEvent)) {
			errors.reject("object.duplicate", new String[]{"event", "the repository"}, null);
			return;
			
		}
		
		/*
		 * Validate the integrity of the translations...
		 * 
		 * No more than one translation language per event
		 * 
		 */
		List<EventTranslationObject> translations = newEvent.getEventTranslations();
		
		if(translations == null || translations.size() < 1) {
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
