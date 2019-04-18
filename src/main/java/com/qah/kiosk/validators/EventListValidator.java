package com.qah.kiosk.validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.util.GenericEventListWrapper;

@Lazy
@Component
public class EventListValidator implements Validator {

	private GenericEventPartialValidator genericEventPartialValidator;
	
	private EventValidatorUtility validatorUtility;
	
	
	@Autowired
	public void setGenericEventPartialValidator(GenericEventPartialValidator genericEventPartialValidator) {
		this.genericEventPartialValidator = genericEventPartialValidator;
	}
	
	@Autowired
	public void setEventValidatorUtility(EventValidatorUtility validatorUtility) {
		this.validatorUtility = validatorUtility;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return GenericEventListWrapper.class.isAssignableFrom(clazz);
	}
	
	/*
	 * Validation:
	 * 
	 * For each event:
	 * 
	 * 1. id must be null
	 * 2. defaultTranslationId must be null
	 * 
	 * 3.GenericEventPartialValidator:
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
	 * 4. EventValidatorUtility:
	 * 		1. No duplicates within List<GenericEvent> provided
	 * 
	 * 5. EventValidatorUtility:
	 * 		1. No duplicates with List<GenericEvent> in repository
	 * 
	 * 6. EventValidatorUtility:
	 * 		1. Valid data in translations
	 * 
	 * 7. EventValidatorUtility:
	 * 		1. No duplicate translations
	 * 
	 */
	@Override
	public void validate(Object obj, Errors errors) {
		
		
		List<GenericEvent> events = ((GenericEventListWrapper)obj).getEvents();
		
		
		if(events == null || events.size() < 1) {
			// no content
			return;
		}

		LocalDate eventStartDate = null;
		LocalDate eventEndDate = null;
		String dataLanguage = null;
		StartDateEndDateWrapper dateWrapper = null;
		List<EventTranslationObject> translations = null;
		
		Map<StartDateEndDateWrapper, List<GenericEvent>> dateToEventMap = new HashMap<>();
		List<GenericEvent> processedEvents = null;
		
		boolean duplicatesInList;
		
		int errorCount = 0;
		int translationCount = 0;
				
		int counter = 0;
		for(GenericEvent event : events) {
			errors.pushNestedPath("events[" + counter + "]");

			if(event.getId() != null) {
				errors.rejectValue("id", "property.must_be_null", new String[]{"id"}, null);
				
			}
			
			if(event.getDefaultTranslationId() != null) {
				errors.rejectValue("defaultTranslationId", "property.must_be_null", new String[]{"defaultTranslationId"}, null);
				
			}

			errorCount = errors.getErrorCount();

			genericEventPartialValidator.validate(event, errors);

			if(errorCount != errors.getErrorCount()) {
				// fatal errors... continue to next iteration
				errors.popNestedPath();
				continue;
			}
			
			
			/*
			 * DUPLICATE EVENTS?	
			 * 1. Within provided List?
			 * 2. With repository?
			 *  
			 */
			
			duplicatesInList = false;

			eventStartDate = event.getEventStartDate();
			eventEndDate = event.getEventEndDate();
			dateWrapper = new StartDateEndDateWrapper(eventStartDate, eventEndDate);

			processedEvents = dateToEventMap.get(dateWrapper);

			duplicatesInList = validatorUtility.hasDuplicatesExhaustiveCheck(event, processedEvents);
			if(duplicatesInList) {

				// this event already exists
				
				// duplicate events will not be validated further (validation is expensive) 
				// - see first iteration of duplicate event for errors, which WAS validated
				errors.popNestedPath();
				errors.rejectValue("events[" + counter + "]", "object.duplicate", new String[]{"event", "provided list"}, null);
				
				continue;
				
			}
			
			// this event is not a duplicate, add it to dateToEventMap
			if(processedEvents != null) {
				processedEvents.add(event);
			} else {
				dateToEventMap.put(dateWrapper, new ArrayList<>(Arrays.asList(event)));
			}

			
			if(validatorUtility.validateEventDateWithRepo(event)) {
				// this event exists in the repository already
				errors.popNestedPath();
				errors.rejectValue("events[" + counter + "]", "object.duplicate", new String[]{"event", "repository"}, null);
				
				continue;
				
			}
			
			
			
			
			/*
			 * Validate the integrity of the translations...
			 * 
			 * No more than one translation language per event
			 * 
			 *		
			*/
			dataLanguage = event.getDataLanguage();
			translations = event.getEventTranslations();
			translationCount = 0;
			
			if(translations != null && translations.size() > 0) {
				
				errorCount = errors.getErrorCount();
				for(EventTranslationObject tr : translations) {
					errors.pushNestedPath("eventTranslations[" + translationCount + "]");
					validatorUtility.validateTranslationInfoNoId(tr, errors);
					translationCount++;
					errors.popNestedPath();
				}
				
				if(errorCount == errors.getErrorCount()) {
					validatorUtility.validateTranslationsNoDuplicate(translations, dataLanguage, errors);
				}
				
			}
			
			counter++;
			
			errors.popNestedPath();

		}
		
		
	}
	


	public static class StartDateEndDateWrapper {
		private LocalDate startDate;
		private LocalDate endDate;
		
		public StartDateEndDateWrapper(LocalDate startDate, LocalDate endDate) {
			this.startDate = startDate;
			this.endDate = endDate;
		}
		
		public StartDateEndDateWrapper setStartDate(LocalDate startDate) {
			this.startDate = startDate;
			
			return this;
		}
		
		public StartDateEndDateWrapper setEndDate(LocalDate endDate) {
			this.endDate = endDate;
			
			return this;
		}
		
		public LocalDate getStartDate() {
			
			return this.startDate;
		}
		
		public LocalDate getEndDate() {
			
			return this.endDate;
		}
		
		@Override
		public boolean equals(Object obj) {

			if(this == obj) {

				return true;
			}
			
			if (obj == null) {

				return false;
			}
			
			if (obj.getClass() != getClass()) {

				return false;
			}
			
			StartDateEndDateWrapper other = (StartDateEndDateWrapper)obj;
			
			if (startDate == null) {
				if (other.startDate != null)
					return false;
			}
			
			else if (!startDate.equals(other.startDate)){

				return false;
			}
			
			if (endDate == null) {
				if (other.endDate != null)

					return false;
			}
			
			else if (!endDate.equals(other.endDate)){

				return false;
			}
			
			return true;
			
		}
		
		@Override
		public int hashCode() {
		    int hash = 7;
		    hash = 37 * hash + (startDate == null ? 0 : startDate.toString().hashCode());
		    hash = 37 * hash + (endDate == null ? 0 : endDate.toString().hashCode());
		    
		    return hash;
		}

		
	}


}
