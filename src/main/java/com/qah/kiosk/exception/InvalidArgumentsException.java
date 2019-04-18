package com.qah.kiosk.exception;

public class InvalidArgumentsException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7362472113100207778L;

	public InvalidArgumentsException(String message) {
		super(message);
	}
	
	public InvalidArgumentsException() { }
}
