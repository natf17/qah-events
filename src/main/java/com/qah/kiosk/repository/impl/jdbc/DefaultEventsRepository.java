package com.qah.kiosk.repository.impl.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.EventRepository;
import com.qah.kiosk.repository.EventsRepository;
import com.qah.kiosk.repository.TranslationRepository;
import com.qah.kiosk.util.EventType;

/*
 * All the getEvents* methods, if not provided a contentLang param, will return the event in its defaultLang
 */
@Lazy
@Repository
public class DefaultEventsRepository implements EventsRepository {
	
	private JdbcTemplate jdbcTemplate;
		
	private EventRepository eventRepo;
	
	private TranslationRepository translationRepo;
	
	private EventRowMapper eventRowMapper;

	private static final String GET_ALL_TYPES_EVENTS_BETWEEN = "SELECT * FROM EVENTS WHERE NOT(eventStartDate < ? AND eventEndDate < ? OR ? < eventStartDate AND ? < eventEndDate)";
	private static final String GET_SELECTED_TYPES_EVENTS_BETWEEN = GET_ALL_TYPES_EVENTS_BETWEEN +  " AND eventType = ?";

	private static final String GET_ALL_TYPES_EVENTS_AFTER = "SELECT * FROM EVENTS WHERE NOT(eventStartDate < ? and eventEndDate < ?)";	
	private static final String GET_SELECTED_TYPES_EVENTS_AFTER = GET_ALL_TYPES_EVENTS_AFTER + " AND eventType = ?";

	private static final String GET_ALL_TYPES_EVENTS_BEFORE = "SELECT * FROM EVENTS WHERE eventStartDate < ? OR eventEndDate < ?";
	private static final String GET_SELECTED_TYPES_EVENTS_BEFORE = GET_ALL_TYPES_EVENTS_BEFORE + " AND eventType = ?";


	private static final String GET_ALL_TYPES_EVENTS_STRICTLY_ON = "SELECT * FROM EVENTS WHERE eventStartDate = ? AND eventEndDate = ?";

	private static final String GET_SELECTED_TYPES_EVENTS_ALL_TIME = "SELECT * FROM EVENTS WHERE eventType = ?";
	private static final String GET_ALL_TYPES_EVENTS_ALL_TIME ="SELECT * FROM EVENTS";
	
	private static final String DELETE_ALL_EVENTS = "DELETE FROM EVENTS";

	@Autowired
	private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Autowired
	private void setEventRepository(EventRepository eventRepo) {
		this.eventRepo = eventRepo;
	}
	
	@Autowired
	private void setTranslationRepository(TranslationRepository translationRepo) {
		this.translationRepo = translationRepo;
	}
	
	@Autowired
	private void setEventRowMapper(EventRowMapper eventRowMapper) {
		this.eventRowMapper = eventRowMapper;
	}
	
	/*
	 * Suppose you want all the events between the dates x and y, and n = 0, 1, ...
	 * 
	 * This method returns:
	 * 
	 * Events with startDate x-n or between x and y and endDate between x and y or y+n
	 * 		This matches events that:
	 * 			- start before (or on) x and end between x and y
	 * 			- start between x and y and end between x and y
	 * 			- start before (or on) x and end after (or on) y
	 * 			- start between x and y and end after (or on) y
	 * 
	 * In other words, events that don't start and end before the startDate, or start and end after the endDate
	 * 
	 * Returns an empty List if no events are found
	 */
	
	@Override
	public List<Event> getEventsBetween(LocalDate startDate, LocalDate endDate, String eventLang, EventType eventType) {
		
		// Get matching events from Events table
		List<EventPiece> matchingEvents = null;
		
		String startDateString = startDate.toString();
		String endDateString = endDate.toString();
		
		if(eventType != null) {
			matchingEvents = jdbcTemplate.query(GET_SELECTED_TYPES_EVENTS_BETWEEN, eventRowMapper, startDateString, startDateString, endDateString, endDateString, eventType.toString());
		} else {
			matchingEvents = jdbcTemplate.query(GET_ALL_TYPES_EVENTS_BETWEEN, eventRowMapper, startDateString, startDateString, endDateString, endDateString);

		}
				
		if(matchingEvents.isEmpty()) {
			return Collections.emptyList();
		}
				
		return this.getEventsWithTranslations(matchingEvents);
		
	}

	/*
	 * This method returns all events that, after the given date, are still ongoing, or that begin after.
	 * 
	 * Returns an empty List if no events are found
	 */
	@Override
	public List<Event> getEventsAfter(LocalDate startDate, String eventLang, EventType eventType) {
		// Get matching events from Events table
		List<EventPiece> matchingEvents = null;

		String startDateString = startDate.toString();

		if(eventType != null) {
			matchingEvents = jdbcTemplate.query(GET_SELECTED_TYPES_EVENTS_AFTER, eventRowMapper, startDateString, startDateString, eventType.toString());
		} else {
			matchingEvents = jdbcTemplate.query(GET_ALL_TYPES_EVENTS_AFTER, eventRowMapper, startDateString, startDateString);

		}
		
		if(matchingEvents.isEmpty()) {
			return Collections.emptyList();
		}		
				
		return this.getEventsWithTranslations(matchingEvents);

	}

	/*
	 * This method returns all events that, before the given date, are still ongoing, or that completed already.
	 * 
	 * Returns an empty List if no events are found
	 */
	@Override
	public List<Event> getEventsBefore(LocalDate endDate, String eventLang, EventType eventType) {
		// Get matching events from Events table
		List<EventPiece> matchingEvents = null;

		String endDateString = endDate.toString();

		if(eventType != null) {
			matchingEvents = jdbcTemplate.query(GET_SELECTED_TYPES_EVENTS_BEFORE, eventRowMapper, endDateString, endDateString, eventType.toString());
		} else {
			matchingEvents = jdbcTemplate.query(GET_ALL_TYPES_EVENTS_BEFORE, eventRowMapper, endDateString, endDateString);

		}
							
		if(matchingEvents.isEmpty()) {
			return Collections.emptyList();
		}			
				
		return this.getEventsWithTranslations(matchingEvents);

	}
	
	/*
	 * Returns an empty List if no events are found
	 */
	@Override
	public List<Event> getEventsAllDates(String eventLang, EventType eventType) {
		// Get matching events from Events table
		List<EventPiece> matchingEvents = null;

		if(eventType != null) {
			matchingEvents = jdbcTemplate.query(GET_SELECTED_TYPES_EVENTS_ALL_TIME, eventRowMapper, eventType.toString());
		} else {
			matchingEvents = jdbcTemplate.query(GET_ALL_TYPES_EVENTS_ALL_TIME, eventRowMapper);

		}

		if(matchingEvents.isEmpty()) {
			return Collections.emptyList();
		}

		return this.getEventsWithTranslations(matchingEvents);
		
	}

	@Override
	public List<Event> saveEventsList(List<Event> events) {
		
		events.forEach(e -> {
			eventRepo.postEvent(e);
			
		});
		
		return events;
	}
	
	/*
	 * Returns an empty List if no events are found
	 */
	@Override
	public List<Event> getEventsStrictlyOn(LocalDate eventStartDate, LocalDate eventEndDate) {
		

		List<EventPiece> eventsOnDate = jdbcTemplate.query(GET_ALL_TYPES_EVENTS_STRICTLY_ON, eventRowMapper, eventStartDate.toString(), eventEndDate.toString());
		
		if(eventsOnDate.isEmpty()) {
			return Collections.emptyList();
		}

		return this.getEventsWithTranslations(eventsOnDate);

	}

	private List<Event> getEventsWithTranslations(List<EventPiece> pieces) {
		List<Event> events = new ArrayList<>();
		pieces.forEach(i -> {
			List<EventTranslation> eventTranslations = translationRepo.getEventTranslationsForEventId(i.getId());
			if(eventTranslations != null && eventTranslations.size() > 0) {
				events.add(Event.createEvent(i, eventTranslations));
			} else {
				events.add(Event.createEventNoTranslation(i));
			}
		});

		return events;
	}

	@Override
	public void deleteAllEvents() {
		jdbcTemplate.update(DELETE_ALL_EVENTS);

		
	}


}
