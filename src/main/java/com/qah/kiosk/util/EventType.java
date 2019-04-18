package com.qah.kiosk.util;

public enum EventType {
	CACO("CA-CO"), 
	CABR("CA-BR"),
	REG("REG"),
	MEM("MEM"),
	OTHER("OTHER");
	
	private String event;
	
	private EventType(String event) {
		this.event = event;
	}
	
	public String toString() {
		return event;
	}

}
