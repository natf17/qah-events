package com.qah.kiosk.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.qah.kiosk.errors.ErrorResource;
import com.qah.kiosk.errors.FieldErrorResource;
import com.qah.kiosk.errors.GlobalErrorResource;
import com.qah.kiosk.exception.InvalidRequestException;
import com.qah.kiosk.exception.NotFoundException;

@ControllerAdvice
public class KioskExceptionHandler extends ResponseEntityExceptionHandler {
	
	private MessageSource messageSource;
	
	@Autowired
	private void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		InvalidRequestException ex = (InvalidRequestException)e;

		List<FieldErrorResource> fieldErrorResources = new ArrayList<FieldErrorResource>();
		List<GlobalErrorResource> globalErrorResources = new ArrayList<GlobalErrorResource>();

		List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();

	    for (FieldError fieldError : fieldErrors) {

	    	FieldErrorResource fieldErrorResource = new FieldErrorResource();

	        fieldErrorResource.setResource(fieldError.getObjectName());

	        fieldErrorResource.setField(fieldError.getField());

	        fieldErrorResource.setCode(messageSource.getMessage(fieldError, null));

	        fieldErrorResource.setMessage(fieldError.getDefaultMessage());

            fieldErrorResources.add(fieldErrorResource);

        }

	    List<ObjectError> globalErrors = ex.getErrors().getGlobalErrors();
	    for (ObjectError globalError : globalErrors) {
	    	GlobalErrorResource globalErrorResource = new GlobalErrorResource();
	    	globalErrorResource.setResource(globalError.getObjectName());
	    	globalErrorResource.setObject(globalError.getObjectName());
	    	globalErrorResource.setCode(messageSource.getMessage(globalError, null));
	    	globalErrorResource.setMessage(globalError.getDefaultMessage());
            globalErrorResources.add(globalErrorResource);
        }
	    
	    
	    
		ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
		
		error.setFieldErrors(fieldErrorResources);
		error.setGlobalErrors(globalErrorResources);
		

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
		
	}
	
	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<Object> handleNotFound(RuntimeException e, WebRequest request) {
		NotFoundException ex = (NotFoundException)e;
		String message = ex.getMessage();
		
		String body = message != null ? message : "";
		

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, body, headers, HttpStatus.NOT_FOUND, request);
		
	}

	
	
}
