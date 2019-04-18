package com.qah.kiosk.domain;

import java.time.LocalDate;
import java.util.List;


public class GenericEvent extends EventTranslationObject implements EventSkeleton {
	private Long id;
	private Long defaultTranslationId;
	private String eventType;
	private String defaultDataLang;
	private LocalDate eventStartDate;
	private LocalDate eventEndDate;
	private List<EventTranslationObject> eventTranslations;
	

	
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

	public void setEventTranslations(List<EventTranslationObject> eventTranslations) {
		this.eventTranslations = eventTranslations;
		
	}

	public List<EventTranslationObject> getEventTranslations() {
		return this.eventTranslations;
	}
	
	public String getDefaultDataLang() {
		return this.defaultDataLang;
	}
	
	public void setDefaultDataLang(String defaultDataLang) {
		this.defaultDataLang = defaultDataLang;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDefaultTranslationId() {
		return this.defaultTranslationId;
	}
	
	public void setDefaultTranslationId(Long defaultTranslationId) {
		this.defaultTranslationId = defaultTranslationId;
	} 

}
