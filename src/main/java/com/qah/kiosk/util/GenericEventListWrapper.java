package com.qah.kiosk.util;

import java.util.List;

import com.qah.kiosk.domain.GenericEvent;

public class GenericEventListWrapper {
	private List<GenericEvent> events;
	
	public GenericEventListWrapper() { }

	
	public GenericEventListWrapper(List<GenericEvent> events) {
		this.events = events;
	}

	public List<GenericEvent> getEvents() {
		return this.events;
	}
	
	public void setEvents(List<GenericEvent> events) {
		this.events = events;
	}
}
