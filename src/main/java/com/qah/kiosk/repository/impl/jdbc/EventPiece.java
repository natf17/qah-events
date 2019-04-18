package com.qah.kiosk.repository.impl.jdbc;


import java.time.LocalDate;


public class EventPiece  {
		
	private Long id;
	private String eventType;
	private String defaultDataLang;
	private LocalDate eventStartDate;
	private LocalDate eventEndDate;
		

	public void setEventType(String eventType) {
		this.eventType = eventType;
		
	}
	
	public String getEventType() {
		return this.eventType;
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
		
}
