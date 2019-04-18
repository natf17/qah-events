package com.qah.kiosk.repository;

import com.qah.kiosk.entity.Event;

public interface EventRepository {

	Event getEvent(Long id);
	
	Event postEvent(Event event);

	Event putEvent(Event event);

	int deleteEvent(Long id);
	

}
