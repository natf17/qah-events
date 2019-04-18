package com.qah.kiosk.domain;

import java.time.LocalDate;

import com.qah.kiosk.entity.EventTranslation;

/*
 * This class is a merge of EventSkeleton and EventTranslation
 * to create a fully translated event
 */
public class SingleTranslatedEvent extends EventTranslation implements EventSkeleton {
	private String defaultDataLang;
	private String eventType;
	private LocalDate eventStartDate;
	private LocalDate eventEndDate;
	private Long currentTranslationId;

	
	
	@Override
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	@Override
	public String getEventType() {
		return this.eventType;
	}

	@Override
	public void setEventStartDate(LocalDate eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	
	@Override
	public LocalDate getEventStartDate() {
		return this.eventStartDate;
	}
	
	@Override
	public void setEventEndDate(LocalDate eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	
	@Override
	public LocalDate getEventEndDate() {
		return this.eventEndDate;
	}
	
	@Override
	public String getDefaultDataLang() {
		return this.defaultDataLang;
	}
	
	@Override
	public void setDefaultDataLang(String defaultDataLang) {
		this.defaultDataLang = defaultDataLang;
		
	}
	
	public Long getCurrentTranslationId() {
		return this.currentTranslationId;
	}
	
	public void setCurrentTranslationId(Long currentTranslationId) {
		this.currentTranslationId = currentTranslationId;
	}
	

}
