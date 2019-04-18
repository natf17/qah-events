package com.qah.kiosk.validators;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.util.EventType;

@Lazy
@Component
public class EventTypeValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg) {
		return arg.isAssignableFrom(String.class);
	}

	@Override
	public void validate(Object eventType, Errors errors) {
		String evType = new String((String)eventType);
		evType.toUpperCase();
		
		for(EventType validEventType : EventType.values()) {
			
			if(evType.equals(validEventType.toString())) {
				return;
			} else if(evType.equals(validEventType.name())) {
				return;
			}
		}
		
		errors.rejectValue("eventType", "property.value_unresolvable", new String[]{"eventType"}, null);
		
	}

}
