package com.qah.kiosk.util;

import java.util.List;

import com.qah.kiosk.domain.SingleTranslatedEvent;

public class TranslatedEventListWrapper {
	private List<SingleTranslatedEvent> events;
	
	public TranslatedEventListWrapper() { }

	
	public TranslatedEventListWrapper(List<SingleTranslatedEvent> events) {
		this.events = events;
	}

	public List<SingleTranslatedEvent> getEvents() {
		return this.events;
	}
	
	public void setEvents(List<SingleTranslatedEvent> events) {
		this.events = events;
	}

}
