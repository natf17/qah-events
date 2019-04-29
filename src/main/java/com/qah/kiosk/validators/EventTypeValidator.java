package com.qah.kiosk.validators;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.qah.kiosk.repository.EventTypeRepository;
import com.qah.kiosk.util.EventType;

@Lazy
@Component
public class EventTypeValidator implements Validator, InitializingBean {

	private EventTypeRepository repo;
	private List<EventType> validEventTypes;
	
	@Autowired(required = false)
	public void setEventTypeRepository(EventTypeRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public boolean supports(Class<?> arg) {
		return arg.isAssignableFrom(String.class);
	}

	@Override
	public void validate(Object eventType, Errors errors) {
		String evType = (String)eventType;
		
		try {
			validEventTypes.stream()
								.filter(i -> i.getAbbr().equalsIgnoreCase(evType))
								.findFirst()
								.get();
		} catch(NoSuchElementException ex) {
			errors.rejectValue("eventType", "property.value_unresolvable", new String[]{"eventType"}, null);
		}
				
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(repo != null) {
			try {
				validEventTypes = repo.getEventTypes();
				return;
			} catch(Exception ex) {
				// if anything goes wrong, add defaults
			}

		}
		
		setDefaults();
		
		
	}
	
	protected void setDefaults() {
		/*
		 * Add default event types:
		 * 
		 */
		
		System.out.println("Setting default event types");
		validEventTypes = Arrays.asList(
				new EventType("CA-CO", "Circuit"),
				new EventType("REG", "Regional"),
				new EventType("CA-BR", "Circuit"),
				new EventType("MEM", "Memorial")
				);
	}

}
