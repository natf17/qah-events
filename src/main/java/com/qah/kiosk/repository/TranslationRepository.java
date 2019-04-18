package com.qah.kiosk.repository;

import java.util.List;

import com.qah.kiosk.entity.EventTranslation;

public interface TranslationRepository {
	
	
	List<EventTranslation> getEventTranslationsForEventId(Long id);
		
	EventTranslation saveTranslation(Long eventId, EventTranslation tr);
	
	int deleteTranslation(Long id);
	
	EventTranslation getTranslation(Long translationId);
		
	EventTranslation putTranslation(EventTranslation tr);

	Long getEnclosingEventId(Long translationId);


}
