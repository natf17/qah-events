package com.qah.kiosk.entity;

import java.time.LocalDate;
import java.util.List;

import com.qah.kiosk.repository.impl.jdbc.EventPiece;

public class Event {
	private Long id;
	private String eventType;
	private LocalDate eventStartDate;
	private LocalDate eventEndDate;
	private String defaultDataLang;
	private List<EventTranslation> eventTranslations;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getEventType() {
		return this.eventType;
	}
	
	public void setEventStartDate(LocalDate eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	
	public LocalDate getEventStartDate() {
		return this.eventStartDate;
	}
	
	public void setEventEndDate(LocalDate eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	
	public LocalDate getEventEndDate() {
		return this.eventEndDate;
	}
	
	public String getDefaultDataLang() {
		return this.defaultDataLang;
	}
	
	public void setDefaultDataLang(String defaultDataLang) {
		this.defaultDataLang = defaultDataLang;
	}
	
	public List<EventTranslation> getEventTranslations() {
		return this.eventTranslations;
	}
	
	public void setEventTranslations(List<EventTranslation> eventTranslations) {
		this.eventTranslations = eventTranslations;
	}
	
	public static Event createEvent(EventPiece e, List<EventTranslation> tr) {
		Event event = createEventNoTranslation(e);
		event.setEventTranslations(tr);
		
		return event;
	}
	
	public static Event createEventNoTranslation(EventPiece e) {
		Event event = new Event();
		event.setId(e.getId());
		event.setDefaultDataLang(e.getDefaultDataLang());
		event.setEventEndDate(e.getEventEndDate());
		event.setEventStartDate(e.getEventStartDate());
		event.setEventType(e.getEventType());
		
		return event;
	}
}
