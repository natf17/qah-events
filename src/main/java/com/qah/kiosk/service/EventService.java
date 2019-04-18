package com.qah.kiosk.service;

import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.domain.SingleTranslatedEvent;

public interface EventService {

	GenericEvent getEvent(Long id);
	
	GenericEvent postEvent(GenericEvent genericEvent);

	GenericEvent putEvent(GenericEvent genericEvent);

	boolean deleteEvent(Long id);
	
	GenericEvent getEnclosingEvent(Long translationId);
	
	SingleTranslatedEvent getTranslatedEvent(Long id, String dataLanguage);

}
