package com.qah.kiosk.exception;

public class TranslationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7129640570743290038L;

	public TranslationNotFoundException(String message) {
		super(message);
	}
	
	public TranslationNotFoundException() { }
}
