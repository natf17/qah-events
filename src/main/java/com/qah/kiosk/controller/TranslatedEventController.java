package com.qah.kiosk.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.exception.NotFoundException;
import com.qah.kiosk.service.EventService;

@RestController
@RequestMapping("/event/translated")
public class TranslatedEventController {
	
	private EventService eventService;
	
	@Autowired
	private void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	/*
	 * GET an event by id in given language
	 * 
	 * 
	 * ---- GET /event/translated/6543?lang=sp
	 *
	 *
	 * ---> data sent in Spanish
	 * 
	 * 
	 * Sample JSON response:
	 * 
	 * {
	 * 		"id": 6543,
	 * 		"currentTranslationId": 567
     * 		"eventLanguage": "en",
     * 		"dataLanguage": "sp",
     * 		"eventTitle": "Sea valiente!",
     * 		"comments": "nada",
     * 		"eventType": "REG",
     * 		"defaultDataLang: "en",
     * 		"eventStartDate": "2018-07-06",
     * 		"eventEndDate": "2018-07-08"
  	 *	}
  	 *
	 * 	Throws:
 	 *  	- NotFoundException: if event doesn't exist
 	 *  
 	 *  If successful, returns:
 	 *  	- 200
	 * 
	 */
	@RequestMapping(path="/{id}", method=RequestMethod.GET)
	public ResponseEntity<SingleTranslatedEvent> getEvent(Locale locale, @PathVariable(name="id") Long id) {
		
		// any exception thrown will be caught in the controller advice
		SingleTranslatedEvent postedEvent = eventService.getTranslatedEvent(id, locale.getLanguage());
		
		if(postedEvent == null) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(postedEvent, HttpStatus.OK);
		
	}
}
