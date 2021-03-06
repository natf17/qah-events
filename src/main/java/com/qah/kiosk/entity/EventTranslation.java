package com.qah.kiosk.entity;


public class EventTranslation {

	private String eventLanguage;
	private String dataLanguage;
	private String eventTitle;
	private String comments;
	private Long id;
	
	public String getEventLanguage() {
		return this.eventLanguage;
	}
	
	public void setEventLanguage(String eventLanguage) {
		this.eventLanguage = eventLanguage;
	}
	
	public String getDataLanguage() {
		return this.dataLanguage;
	}
	
	public void setDataLanguage(String dataLanguage) {
		this.dataLanguage = dataLanguage;
	}
	
	public String getEventTitle() {
		return this.eventTitle;
	}
	
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
