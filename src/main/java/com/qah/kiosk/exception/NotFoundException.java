package com.qah.kiosk.exception;

public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9000682319132887783L;
	
	public NotFoundException() { }
	
	public NotFoundException(String message) {
		super (message);
	}
	

}
