package com.qah.kiosk.exception;

import org.springframework.validation.Errors;

public class InvalidRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4852710119171242120L;
	
	private Errors errors;
	
	public InvalidRequestException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}
	
	public Errors getErrors() {
		return errors;
	}

}
