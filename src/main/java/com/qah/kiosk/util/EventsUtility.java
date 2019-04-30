package com.qah.kiosk.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.qah.kiosk.domain.EventSkeleton;
import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.domain.SingleTranslatedEvent;

/*
 * This class contains utility methods to "translate" between domain objects
 */
public class EventsUtility {
	
	public static SingleTranslatedEvent transformGenericToSingleTranslatedEvent(GenericEvent ge, String contentLang) {
		
		List<EventTranslationObject> ets = ge.getEventTranslations();
		
		if(ets == null || ets.size() < 1) {
			// A GenericEvent must have EventTranslations

			return null;
		}
		
		List<EventTranslationObject> desiredTrList = ets.stream()
											.filter(i -> {
												boolean t = i.getDataLanguage().equals(contentLang);
												System.out.println(t);
												return t;
											})
											.limit(1)
											.collect(Collectors.toList());
		
		if(desiredTrList == null || desiredTrList.isEmpty()) {
			return null;
		}
		
		
		return createSingleTranslatedEvent(ge, desiredTrList.get(0));
		
	}


	public static List<SingleTranslatedEvent> sortSingleTranslatedEvents(List<SingleTranslatedEvent> events) {
		if(events == null || events.size() == 0) {

			return new ArrayList<>();
		}

		Collections.sort(events, new SingleTranslatedEventComporatator());
		
		return events;
	}
	
	static class SingleTranslatedEventComporatator implements Comparator<SingleTranslatedEvent> {

		@Override
		public int compare(SingleTranslatedEvent event1, SingleTranslatedEvent event2) {
			
			// the earlier, the smaller
			LocalDate dateStart1 = event1.getEventStartDate();
			LocalDate dateStart2 = event2.getEventStartDate();
			
			if(dateStart1.isAfter(dateStart2)) {
				return 1;
			}
			
			if(dateStart1.isBefore(dateStart2)) {
				return -1;
			}
			
			// if same start day... try by end date
			LocalDate dateEnd1 = event1.getEventEndDate();
			LocalDate dateEnd2 = event2.getEventEndDate();
			
			if(dateEnd1.isAfter(dateEnd2)) {
				return 1;
			}
			
			if(dateEnd1.isBefore(dateEnd2)) {
				return -1;
			}
			
			// is same start and end date... sort by theme title
			
			String eventTitle1 = event1.getEventTitle();
			String eventTitle2 = event2.getEventTitle();
			
			return eventTitle1.compareTo(eventTitle2);
			
			
		}
		
	}
	
	
	public static SingleTranslatedEvent createSingleTranslatedEvent(EventSkeleton ge, EventTranslationObject et) {
		
		SingleTranslatedEvent ste = new SingleTranslatedEvent();
		
		ste.setId(ge.getId());
		ste.setEventType(ge.getEventType());
		ste.setEventTitle(et.getEventTitle());
		ste.setEventStartDate(ge.getEventStartDate());
		ste.setEventEndDate(ge.getEventEndDate());
		ste.setEventLanguage(et.getEventLanguage());
		ste.setDefaultDataLang(ge.getDefaultDataLang());
		ste.setComments(et.getComments());
		ste.setDataLanguage(et.getDataLanguage());
		ste.setCurrentTranslationId(et.getId());
		
		return ste;
	}
	
}
