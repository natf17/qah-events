package com.qah.kiosk.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.domain.SingleTranslatedEvent;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.repository.EventRepository;
import com.qah.kiosk.service.EventService;
import com.qah.kiosk.service.TranslationsService;
import com.qah.kiosk.service.utils.DomainEntityUtils;
import com.qah.kiosk.util.EventsUtility;

@Lazy
@Service
public class DefaultEventService implements EventService {

	
	private EventRepository repo;
	private TranslationsService trService;
	
	@Autowired
	public void setEventRepository(EventRepository repo) {
		this.repo = repo;
	}
	
	@Autowired
	public void setTranslationsService(TranslationsService trService) {
		this.trService = trService;
	}
	
	
	/*
	 * Returns null if no event was found;
	 */
	@Override
	public GenericEvent getEvent(Long id) {
		if(id < 0) {
			return null;
		}
		
		Event event = repo.getEvent(id);
		
		if(event == null) {
			return null;
		}
		
		return DomainEntityUtils.eventToGeneric(event);
	}
	

	@Override
	public GenericEvent postEvent(GenericEvent genericEvent) {
		
		return DomainEntityUtils.eventToGeneric(repo.postEvent(DomainEntityUtils.genericToEvent(genericEvent)));
		
	}

	@Override
	public GenericEvent putEvent(GenericEvent genericEvent) {
		
		return DomainEntityUtils.eventToGeneric(repo.putEvent(DomainEntityUtils.genericToEvent(genericEvent)));
		
	}

	/*
	 * Returns false if unsuccessful
	 */
	@Override
	public boolean deleteEvent(Long id) {
		if(repo.deleteEvent(id) != 1) {
			return false;
		}
		
		return true;
		
	}
	
	/*
	 * Returns null if enclosing event does not exist
	 */
	@Override
	public GenericEvent getEnclosingEvent(Long translationId) {
		Long eventId = trService.getParentEventId(translationId);

		if(eventId == null) {
			return null;
		}
		
		return this.getEvent(eventId);
	}

	/*
	 * Returns null if:
	 * 		1. An event with given id does not exist
	 * 		2. A translation with the given language does not exist
	 */
	@Override
	public SingleTranslatedEvent getTranslatedEvent(Long id, String dataLanguage) {
		GenericEvent event = this.getEvent(id);
		
		if(event == null) {
			return null;
		}
		
		if(dataLanguage == null || dataLanguage.isEmpty()) {
			dataLanguage = event.getDefaultDataLang();
			
		}
		
		EventTranslationObject evTr = null;
		
		for(EventTranslationObject tr : event.getEventTranslations()) {
			if(tr.getDataLanguage().equals(dataLanguage)) {
				evTr = tr;
			}
		}
		
		if(evTr == null) {
			return null;
		}
		
		return EventsUtility.createSingleTranslatedEvent(event, evTr);
	}

	

}
