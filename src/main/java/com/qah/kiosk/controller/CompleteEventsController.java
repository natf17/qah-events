package com.qah.kiosk.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.exception.InvalidRequestException;
import com.qah.kiosk.exception.NotFoundException;
import com.qah.kiosk.service.EventsService;
import com.qah.kiosk.service.utils.TimeRange;
import com.qah.kiosk.util.EventType;
import com.qah.kiosk.util.GenericEventListWrapper;
import com.qah.kiosk.validators.EventListValidator;

@RestController
@RequestMapping("/events")
public class CompleteEventsController {
	
	private EventsService eventsService;
	
	private EventListValidator eventListValidator;
	
	@Autowired
	private void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	@Autowired
	private void setEventListValidator(EventListValidator eventListValidator) {
		this.eventListValidator = eventListValidator;
	}

	
	/*
	 * POST a List of events in a generic, event-type-agnostic manner
	 * 
	 *
	 * NOTE: dataLanguage of parent is REQUIRED, it will remain the default dataLanguage for the event
	 * 
	 * 
	 * ---- POST /events
	 * 
	 * 
 	 *  
 	 *  NOTE: The id(in event and translations) and defaultTranslationId(in event) 
	 * 		 fields cannot be set in request
	 * 
	 * 	Throws:
	 * 		- InvalidRequestException: if validation errors are found
	 * 
	 *  If successful, returns:
	 *  	- 201
	 */
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<GenericEventListWrapper> postEventsWithTranslations(@RequestBody @Valid GenericEventListWrapper eventWrapper, Errors errors, Locale locale) {

		if(errors.hasErrors()) {
			throw new InvalidRequestException("", errors);
		}
		
		List<GenericEvent> results = eventsService.saveGenericEventsList(eventWrapper.getEvents());
		
		return new ResponseEntity<>(new GenericEventListWrapper(results), HttpStatus.CREATED);
		
	}
	
	/*
	 * GET events that match the event language, dates, and event type provided
	 * 
	 * 
	 * Sample JSON request body:
	 * 
	 * 
	 * ---- GET /events?eventLang=en&eventType=reg&after=2018-07-01&before=2018-08-2018
	 * 
	 * ---> returns English regional conventions between July and August 2018
	 *
	 *	{
	 * 		events = [
	 * 
	 *  		{
	 *  			id = 54
	 * 				defaultTranslationId = 654
	 * 				eventType = "REG",
	 *  			eventStartDate = "2018-07-06",
	 *  			eventEndDate = "2018-07-08", (optional)
	 *  			eventTitle = "Be courageous",
 	 *  			eventLanguage = "sp",
 	 *  			defaultDataLang
 	 *  			dataLanguage = "en",
 	 *  			comments = "Caution"
 	 *  			eventTranslations=[
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
 	 *  			]
 	 *  
 	 *  		}
 	 *  
 	 *  		{ ... }
 	 *  
 	 *  	]
 	 *  
 	 *  }
 	 *  
 	 *  Throws:
 	 *  	- NotFoundException: if no events are found
 	 *  
 	 *  If successful, returns:
 	 *  	- 200
	 * 
	 */
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<GenericEventListWrapper> getAllEvents(Locale locale, @RequestParam(value="eventLang", required=false)String eventLang, @RequestParam(value="eventType", required=false)EventType eventType, @RequestParam(value="after", required=false)LocalDate startDate, @RequestParam(value="before", required=false)LocalDate endDate) {
		
		List<GenericEvent> events = null;
		
		events = eventsService.processGet(TimeRange.createTimeRange(startDate, endDate), eventLang, eventType);
		
		if(events == null || events.size() == 0) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(new GenericEventListWrapper(events), HttpStatus.OK);
		
		
	}
	
	/*
	 * DELETE events (along with translations) that match parameters
	 * 
	 * ---- DELETE /events?eventLang=en&eventType=reg&after=2018-07-01&before=2018-08-2018
	 * 
	 * ---> deletes all English regional conventions on July
	 * 
	 * 
	 * NOTE: If no parameters provided, all events will be deleted
	 * 
	 * NOTE: If not provided, eventLang defaults to locale language
	 * 
	 * 
 	 *  Throws:
 	 *  	- NotFoundException: if no events are found
 	 *  
 	 *  If successful, returns:
 	 *  	- 204
	 * 
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public ResponseEntity<List<GenericEvent>> deleteEvents(Locale locale, @RequestParam(value="eventLang", required=false)String eventLang, @RequestParam(value="eventType", required=false)EventType eventType, @RequestParam(value="after", required=false)LocalDate startDate, @RequestParam(value="before", required=false)LocalDate endDate) {
		
		boolean isSuccessful = eventsService.processDelete(TimeRange.createTimeRange(startDate, endDate), locale.getLanguage(), eventType);	
		
		if(!isSuccessful) {
			throw new NotFoundException();
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(eventListValidator);
	}

}
