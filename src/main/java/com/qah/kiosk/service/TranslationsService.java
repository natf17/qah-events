package com.qah.kiosk.service;

import com.qah.kiosk.domain.EventTranslationObject;

public interface TranslationsService {

	EventTranslationObject postTranslation(Long eventId, EventTranslationObject tr);
	
	EventTranslationObject putTranslation(EventTranslationObject tr);
	
	boolean deleteTranslation(Long translationId);
	
	EventTranslationObject getTranslation(Long translationId);
		
	Long getParentEventId(Long translationId);
}
