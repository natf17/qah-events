package com.qah.kiosk.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.repository.EventsRepository;
import com.qah.kiosk.service.EventService;
import com.qah.kiosk.service.EventsService;
import com.qah.kiosk.service.utils.DomainEntityUtils;
import com.qah.kiosk.service.utils.TimeRange;
import com.qah.kiosk.service.utils.TimeRangeType;
import com.qah.kiosk.util.EventType;
import com.qah.kiosk.util.EventsUtility;

@Lazy
@Service
public class DefaultEventsService implements EventsService {
	
	private EventsRepository repo;
	
	private EventService eventService;
		
	@Autowired
	public void setEventsRepository(EventsRepository repo) {
		this.repo = repo;
	}
	
	@Autowired
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	@Override
	public List<GenericEvent> saveGenericEventsList(List<GenericEvent> events) {
		
		List<Event> newEvents = repo.saveEventsList(events.stream()
													.map(n -> DomainEntityUtils.genericToEvent(n))
													.collect(Collectors.toList()));
		
		
		return newEvents.stream()
							.map(n -> DomainEntityUtils.eventToGeneric(n))
							.collect(Collectors.toList());
		
	}
	
	@Override
	public List<GenericEvent> getGenericEventsStrictlyOn(LocalDate eventStartDate, LocalDate eventEndDate) {	
		
		return (repo.getEventsStrictlyOn(eventStartDate, eventEndDate)).stream()
											.map(n -> DomainEntityUtils.eventToGeneric(n))
											.collect(Collectors.toList());
	}
	
	/*
	 * Returns true if an event was deleted.
	 * 
	 * However, it does not make sure matching events were actually deleted.
	 */
	@Override
	public boolean processDelete(TimeRange timeRange, String eventLang, EventType eventType) {

		List<GenericEvent> events = processGet(timeRange, eventLang, eventType);
		
		if(events.size() < 1) {
			return false;
		}
		
		this.handleDelete(events);
		
		return true;
		
	}
		
	private void handleDelete(List<GenericEvent> events) {
				
		events.stream()
					.filter(e -> e.getId() != null)
					.forEach(e -> eventService.deleteEvent(e.getId()));
		
	}

	/*
	 * Returns an empty List if no events found
	 */
	@Override
	public List<SingleTranslatedEvent> processGet(TimeRange timeRange, String eventLang, EventType eventType,
			String contentLang) {
		List<GenericEvent> matchingEvents = processGet(timeRange, eventLang, eventType);
		
		if(matchingEvents.size() < 1) {
			return Collections.emptyList();
		}

		List<SingleTranslatedEvent> events = matchingEvents.stream()
																.map(i -> EventsUtility.transformGenericToSingleTranslatedEvent(i, contentLang))
																.filter(i -> i != null)
																.collect(Collectors.toList());

		return EventsUtility.sortSingleTranslatedEvents(events);
		
	}
	
	/*
	 * Returns an empty List if no events found
	 */
	@Override
	public List<GenericEvent> processGet(TimeRange timeRange, String eventLang, EventType eventType) {

		List<Event> matchingEvents = new ArrayList<>();
		
		TimeRangeType timeType = timeRange.getTimeRangeType();
		switch(timeType) {
			case BEFORE: 
				matchingEvents = repo.getEventsBefore(timeRange.getEventEndDate(), eventLang, eventType);
				break;
			
			case AFTER:
				matchingEvents = repo.getEventsAfter(timeRange.getEventStartDate(), eventLang, eventType);
				break;
			
			case BETWEEN:
				matchingEvents = repo.getEventsBetween(timeRange.getEventStartDate(), timeRange.getEventEndDate(), eventLang, eventType);
				break;
				
			case ALL:
				matchingEvents = repo.getEventsAllDates(eventLang, eventType); 
				break;
		
			default:
				;
		
		}
		
		if(matchingEvents.size() < 1) {

			return Collections.emptyList();
		}

		return matchingEvents.stream()
								.map(n -> DomainEntityUtils.eventToGeneric(n))
								.collect(Collectors.toList());
		
		
	}

	
}
