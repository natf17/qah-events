package com.qah.kiosk.repository;

import java.time.LocalDate;
import java.util.List;

import com.qah.kiosk.entity.Event;
import com.qah.kiosk.util.EventType;

public interface EventsRepository {
	List<Event> getEventsBetween(LocalDate startDate, LocalDate endDate, String eventLang, EventType eventType);
		
	List<Event> getEventsAfter(LocalDate startDate, String eventLang, EventType eventType);
	
	List<Event> getEventsBefore(LocalDate endDate, String eventLang, EventType eventType);
	
	List<Event> getEventsAllDates(String eventLang, EventType eventType);
	
	List<Event> saveEventsList(List<Event> events);
	
	List<Event> getEventsStrictlyOn(LocalDate eventStartDate, LocalDate eventEndDate);
	
	void deleteAllEvents();
	
}
