package com.qah.kiosk.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.exception.NotFoundException;
import com.qah.kiosk.service.EventsService;
import com.qah.kiosk.service.utils.TimeRange;
import com.qah.kiosk.util.EventType;
import com.qah.kiosk.util.TranslatedEventListWrapper;


/*
 * For all url paths, changing the locale via a URL parameter ("lang")
 * will return all the data in the locale language
 */


@RestController
@RequestMapping("/events/translated")
public class TranslatedEventsController {
	
	private EventsService eventsService;
	
	
	@Autowired
	private void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	
	/* 
	 * GET all events that match event language, dates, and event type, with multi-language support:
	 * 
	 * 
	 * ---- GET /events/translated?eventLang=en&lang=sp&eventType=REG&after=2018-07-01&before=2018-08-18
	 *
	 *
	 * ---> data sent in Spanish; returns English regional conventions between July and August 2018
	 * 
	 * 
	 * Sample JSON response:
	 * 
	 * {
	 * 	events: [
	 * 		{
     * 			"id": 6543,
	 * 			"currentTranslationId": 567,
     * 			"eventLanguage": "en",
     * 			"dataLanguage": "sp",
     * 			"eventTitle": "Sea valiente!",
     * 			"comments": "nada",
     * 			"eventType": "REG",
     * 			"defaultDataLang: "en",
     * 			"eventStartDate": "2018-07-06",
     * 			"eventEndDate": "2018-07-08"
  	 *		},
  	 *		{
  	 *			"id": 6545,
	 * 			"currentTranslationId": 5365
     * 			"eventLanguage": "en",
     * 			"dataLanguage": "sp",
     * 			"eventTitle": "Sea valiente!",
     * 			"comments": "nada",
     * 			"eventType": "REG",
     * 			"defaultDataLang: "en",
     * 			"eventStartDate": "2018-07-23",
     * 			"eventEndDate": "2018-07-25"
  	 *		}
	 * 	]
	 * }
	 * 
	 * Throws:
 	 *  	- NotFoundException: if event doesn't exist
 	 *  
 	 *  If successful, returns:
 	 *  	- 200
	 * 
	 */
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<TranslatedEventListWrapper> getAllEvents(Locale locale, @RequestParam(value="eventLang", required=false)String eventLang, @RequestParam(value="eventType", required=false)EventType eventType, @RequestParam(value="after", required=false)LocalDate startDate, @RequestParam(value="before", required=false)LocalDate endDate) {

		List<SingleTranslatedEvent> events = null;
		
		events = eventsService.processGet(TimeRange.createTimeRange(startDate, endDate), eventLang, eventType, locale.getLanguage());
		
		if(events == null || events.isEmpty()) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(new TranslatedEventListWrapper(events), HttpStatus.OK);
		
		
	}


}



