package com.qah.kiosk.service;

import java.time.LocalDate;
import java.util.List;

import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.service.utils.TimeRange;
import com.qah.kiosk.util.EventType;

public interface EventsService {
	
	List<GenericEvent> saveGenericEventsList(List<GenericEvent> events);
	
	List<GenericEvent> getGenericEventsStrictlyOn(LocalDate eventStartDate, LocalDate eventEndDate); 
	
	boolean processDelete(TimeRange timeRange, String eventLang, EventType eventType);
	
	List<SingleTranslatedEvent> processGet(TimeRange timeRange, String eventLang, EventType eventType, String contentLang);

	List<GenericEvent> processGet(TimeRange timeRange, String eventLang, EventType eventType);
}
