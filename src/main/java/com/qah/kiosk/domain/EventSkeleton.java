package com.qah.kiosk.domain;

import java.time.LocalDate;

public interface EventSkeleton {
	
	public Long getId();
	public void setId(Long id);
	
	public void setEventType(String eventType);
	public String getEventType();
	
	public void setEventStartDate(LocalDate eventStartDate);
	public LocalDate getEventStartDate();
	
	public void setEventEndDate(LocalDate eventEndDate);
	public LocalDate getEventEndDate();
	
	public String getDefaultDataLang();
	public void setDefaultDataLang(String defaultDataLang);
}
