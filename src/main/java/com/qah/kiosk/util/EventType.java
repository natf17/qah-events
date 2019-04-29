package com.qah.kiosk.util;


public class EventType {
	
	private final String abbr;
	private String name;
	
	public EventType(String abbr) {
		this(abbr, null);
	}
	public EventType(String abbr, String name) {
		this.abbr = abbr;
		this.name = name;
	}
	
	public String getAbbr() {
		return this.abbr;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return abbr;
	}



}
