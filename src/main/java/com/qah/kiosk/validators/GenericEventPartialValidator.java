package com.qah.kiosk.validators;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.domain.GenericEvent;

@Lazy
@Component
public class GenericEventPartialValidator implements Validator {
		
	private EventTypeValidator eventTypeValidator;
	
	@Autowired
	private void setEventTypeValidator(EventTypeValidator eventTypeValidator) {
		this.eventTypeValidator = eventTypeValidator;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return GenericEvent.class.isAssignableFrom(clazz);
	}
	
	/*
	 * Checks:
	 * 
	 * 	1. eventType is not null
	 *  2. eventType is valid (EventTypeValidator)
	 *  3. eventTitle is not null
	 *  4. eventLanguage is not null
	 *  5. dataLanguage and defaultDataLanguage cannot both be null
	 *  6. if both are provided, they must match
	 *  7. startDate is not null
	 *  8. if endDate is not provided, startDate is used
	 *  9. if endDate is provided, it must be after startDate
	 */

	@Override
	public void validate(Object target, Errors errors) {

		GenericEvent event = (GenericEvent)target;
		
		String eventType = null;
		LocalDate eventStartDate = null;
		LocalDate eventEndDate = null;
		String eventTitle = null;
		String eventLanguage = null;
		String dataLanguage = null;
		String defaultDataLang = null;
		
		int errorCount = 0;
		
		boolean failOnMissingFields = false;
		
		
		
		eventType = event.getEventType();
		if(eventType == null) {
			
			errors.rejectValue("eventType", "events.eventType.cannot_be_null");
			errors.rejectValue("eventType", "property.cannot_be__null", new String[]{"eventType"}, null);
			failOnMissingFields = true;
		} else {
			errorCount = errors.getFieldErrorCount("eventType");
			
			eventTypeValidator.validate(eventType, errors);
			
			if(errors.getFieldErrorCount("eventType") > errorCount) {
				failOnMissingFields = true;
			}
			
		}
		

		eventTitle = event.getEventTitle();
		if(eventTitle == null) {
			errors.rejectValue("eventTitle", "property.cannot_be__null", new String[]{"eventTitle"}, null);
			failOnMissingFields = true;

		}
		
		
		eventLanguage = event.getEventLanguage();
		if(eventLanguage == null) {
			errors.rejectValue("eventLanguage", "property.cannot_be__null", new String[]{"eventLanguage"}, null);
			failOnMissingFields = true;

		}
		
		
		dataLanguage = event.getDataLanguage();
		defaultDataLang = event.getDefaultDataLang();

		if(dataLanguage != null) {
			
			if(defaultDataLang != null && !defaultDataLang.equals(dataLanguage)) {
				errors.rejectValue("defaultDataLang", "events.defaultDataLang.inconsistent");
			} else {
				// defaultDataLang is null or defaultDataLang equals dataLanguage
				event.setDefaultDataLang(dataLanguage);
			}
		} else {
			
			if(defaultDataLang == null) {
				errors.rejectValue("defaultDataLang", "events.defaultDataLang.required");

			} else {
				event.setDataLanguage(defaultDataLang);
			}
		}
		
		
		
		eventStartDate = event.getEventStartDate();
		if(eventStartDate == null) {
			errors.rejectValue("eventStartDate", "property.cannot_be__null", new String[]{"eventStartDate"}, null);
			failOnMissingFields = true;
		} else {
			eventEndDate = event.getEventEndDate();
			if(eventEndDate == null) {
				event.setEventEndDate(eventStartDate);

			} else {
				if(eventEndDate.isBefore(eventStartDate)) {
					errors.rejectValue("eventEndDate", "events.eventEndDate.cannot_be_before_eventStartDate");
					failOnMissingFields = true;
				}
			}
		}
	


		
		if(failOnMissingFields) {
			errors.reject("events.missing_required_fields");
			
			return;
		}

	}
	
	

}
