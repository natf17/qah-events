package com.qah.kiosk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.exception.InvalidRequestException;
import com.qah.kiosk.exception.NotFoundException;
import com.qah.kiosk.service.EventService;
import com.qah.kiosk.validators.GenericEventFullValidator;
import com.qah.kiosk.validators.GenericEventPutValidator;


@RestController
@RequestMapping("/event")
public class CompleteEventController {
	
	private EventService eventService;
	
	private GenericEventFullValidator genericEventValidator;
	
	private GenericEventPutValidator genericEventPutValidator;
	
	@Autowired
	private void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
	@Autowired
	private void setGenericEventPutValidator(GenericEventPutValidator genericEventPutValidator) {
		this.genericEventPutValidator = genericEventPutValidator;
	}
	
	@Autowired
	private void setGenericEventFullValidator(GenericEventFullValidator genericEventValidator) {
		this.genericEventValidator = genericEventValidator;
	}

	
	/*
	 * GET an event by id
	 * 
	 * 
	 * Sample JSON request body:
	 *
	 * 
	 * ---- GET /event/54
	 * 
	 * { 
	 * 		id = 54
	 * 		defaultTranslationId = 654
	 * 		eventType = "REG",
	 *  	eventStartDate = "2018-07-06",
	 *  	eventEndDate = "2018-07-08", (optional)
	 *  	eventTitle = "Be courageous",
 	 *  	eventLanguage = "sp",
 	 *  	defaultDataLang
 	 *  	dataLanguage = "en",
 	 *  	comments = "Caution"
 	 *  	eventTranslations=[
 	 *  					{
 	 *  						id = 5465,
 	 *  						eventTitle= "Sea valiente",
 	 *  						eventLanguage= "esp"
 	 *  						dataLanguage="sp",
 	 *  						comments= "Cuidado"
 	 *  					},
 	 *  					{
 	 *  						id = 8765,
 	 *  						eventTitle= "Soix courageux",
 	 *  						eventLanguage= "esp"
 	 *  						dataLanguage="fr",
 	 *  						comments= "Mise en garde"
 	 * 						}
 	 *  
 	 *  	]
 	 *  
 	 *  }
 	 *  
	 * Throws:
 	 *  	- NotFoundException: if event doesn't exist
 	 *  
 	 *  If successful, returns:
 	 *  	- 200
	 */
	
	@RequestMapping(path="/{id}", method=RequestMethod.GET)
	public ResponseEntity<GenericEvent> getEvent(@PathVariable(name="id") Long id) {
		
		// any exception thrown will be caught in the controller advice
		GenericEvent postedEvent = eventService.getEvent(id);
		
		if(postedEvent == null) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(postedEvent, HttpStatus.OK);
		
	}
	
	
	/*
	 * POST a (new) event
	 * 
	 * ---- POST /event
	 * 
	 * NOTE: The id(in event and translations) and defaultTranslationId(in event) 
	 * 		 fields cannot be set in request
	 * 
	 * Throws:
	 * 		- InvalidRequestException: if validation errors are found
	 * 
	 *  If successful, returns:
	 *  	- 201
	 * 
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<GenericEvent> postEvent(@Valid @RequestBody GenericEvent newEvent, Errors errors) {
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("", errors);
		}
		
		// any exception thrown will be caught in the controller advice
		GenericEvent postedEvent = eventService.postEvent(newEvent);
		
		return new ResponseEntity<>(postedEvent, HttpStatus.CREATED);
		
	}
	
	/*
	 * PUT an existing event
	 * 
	 * ---- PUT /event/54
	 * 
	 * Note: The id provided in the url path overrides any provided in the body request
	 * 
	 * This updated event will not be checked against repository for duplicates
	 * 
	 * Throws:
	 * 		- InvalidRequestException: if validation errors are found
	 * 
	 *  If successful, returns:
	 *  	- 204
	 * 
	 */
	@RequestMapping(path="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<GenericEvent> putEvent(@RequestBody GenericEvent newEvent, Errors errors, @PathVariable("id") Long id) {
		
		newEvent.setId(id);
		
		genericEventPutValidator.validate(newEvent, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("", errors);
		}

		// any exception thrown will be caught in the controller advice
		newEvent = eventService.putEvent(newEvent);
			
		return new ResponseEntity<>(newEvent, HttpStatus.NO_CONTENT);
		
	}
	
	/*
	 * DELETE an event
	 * 
	 * ---- DELETE /event/43
	 * 
	 *  Throws:
	 * 		- NotFoundException: if event doesn't exist
	 * 
	 *  If successful, returns:
	 *  	- 204	 
	 */
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<GenericEvent> deleteEvent(@PathVariable(name="id") Long id) {
		
		boolean isSuccessful = eventService.deleteEvent(id);
		
		if(!isSuccessful) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(genericEventValidator);
	}

}
