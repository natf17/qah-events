package com.qah.kiosk.controller;

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

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.exception.InvalidRequestException;
import com.qah.kiosk.exception.NotFoundException;
import com.qah.kiosk.service.TranslationsService;
import com.qah.kiosk.validators.EventTranslationValidator;

@RestController
@RequestMapping("/translations")
public class TranslationsController {
	
	private TranslationsService translationService;
	
	private EventTranslationValidator translationValidator;
	
	@Autowired
	private void setEventTranslationValidator(EventTranslationValidator translationValidator) {
		this.translationValidator = translationValidator;
	}
	
	@Autowired
	private void setTranslationsService(TranslationsService translationService) {
		this.translationService = translationService;
	}
	
	/*
	 * GET a translation by id
	 * 
	 * 
	 * Sample JSON request body:
	 * 
	 * 
	 * ---- GET /translations/5465
	 * 
	 * 	{
 	 *  	id = 5465,
 	 *  	eventTitle= "Sea valiente",
 	 *  	eventLanguage= "ing",
 	 *  	dataLanguage="sp",
 	 *  	comments= "Cuidado"
 	 *  }
 	 *  
 	 *  Throws:
 	 *  	- NotFoundException: if translation doesn't exist
 	 *  
 	 *  If successful, returns:
 	 *  	- 200
	 */
	@RequestMapping(path="/{id}", method=RequestMethod.GET)
	public ResponseEntity<EventTranslationObject> getTranslation(@PathVariable(name="id") Long id) {
		
		// any exception thrown will be caught in the controller advice
		EventTranslationObject translation = translationService.getTranslation(id);
		
		if(translation == null) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(translation, HttpStatus.OK);
		
	}
	
	/*
	 * POST a translation
	 * 
	 * ---- POST /translations/event/654
	 * 
	 * ---> POST a new translation to the event with the given id
	 * 
	 * NOTE: The id cannot be set in request
	 * 
	 *  Throws:
	 * 		- InvalidRequestException: if validation errors are found
	 * 
	 *  If successful, returns:
	 *  	- 201
	 */
	
	@RequestMapping(path="/event/{id}", method=RequestMethod.POST)
	public ResponseEntity<EventTranslationObject> postTranslation(@RequestBody EventTranslationObject tr, Errors errors, @PathVariable(name="id") Long id) {
		// any exception thrown will be caught in the controller advice
		translationValidator.validate(tr, id, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("", errors);

		}
		
		EventTranslationObject response = translationService.postTranslation(id, tr);
		
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
		
	}
	
	/*
	 * PUT a translation
	 * 
	 * 
	 * ---- PUT /translations/54
	 * 
	 * Note: The id provided in the url path overrides any provided in the body request
	 * 
	 * 
	 *  Throws:
	 * 		- InvalidRequestException: if validation errors are found
	 * 
	 *  If successful, returns:
	 *  	- 204
	 * 
	 */
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<EventTranslationObject> putTranslation(@RequestBody EventTranslationObject tr, Errors errors, @PathVariable(name="id") Long id) {
		tr.setId(id);
		
		translationValidator.validate(tr, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("", errors);

		}
		
		translationService.putTranslation(tr);
		
		
		return new ResponseEntity<>(tr, HttpStatus.NO_CONTENT);
		
	}
	
	/*
	 * DELETE a translation
	 * 
	 * ---- DELETE /translation/97
	 * 
	 *  Throws:
	 * 		- NotFoundException: if translation doesn't exist
	 * 
	 *  If successful, returns:
	 *  	- 204
	 * 
	 */
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<EventTranslationObject> deleteTranslation(@PathVariable(name="id") Long id) {
		
		// any exception thrown will be caught in the controller advice
		boolean isSuccessful = translationService.deleteTranslation(id);
		
		if(!isSuccessful) {
			throw new NotFoundException();
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(translationValidator);
	}

}
