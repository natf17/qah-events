package com.qah.kiosk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.TranslationRepository;
import com.qah.kiosk.service.TranslationsService;
import static com.qah.kiosk.service.utils.DomainEntityUtils.domainTranslationToEntity;
import static com.qah.kiosk.service.utils.DomainEntityUtils.entityTranslationToDomain;

@Lazy
@Service
public class DefaultEventTranslationService implements TranslationsService {

	
	private TranslationRepository repo;
	
	@Autowired
	public void setTranslationRepository(TranslationRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public EventTranslationObject postTranslation(Long eventId, EventTranslationObject tr) {
		EventTranslation domTr = domainTranslationToEntity(tr);
		domTr = repo.saveTranslation(eventId, domTr);
		
		return entityTranslationToDomain(domTr);

	}

	@Override
	public EventTranslationObject putTranslation(EventTranslationObject tr) {
		EventTranslation domTr = domainTranslationToEntity(tr);
		domTr = repo.putTranslation(domTr);
		
		return entityTranslationToDomain(domTr);

	}

	/*
	 * Returns true if a translation was deleted
	 */
	@Override
	public boolean deleteTranslation(Long translationId) {
		
		int rowsAffected = repo.deleteTranslation(translationId);
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;

	}

	/*
	 * Returns null if an object with the provided id is not found.
	 */
	@Override
	public EventTranslationObject getTranslation(Long translationId) {

		if(translationId < 0) {
			return null;
		}
	
		EventTranslation translation = repo.getTranslation(translationId);
		
		if(translation == null) {
			return null;
		}
		
		return entityTranslationToDomain(translation);
	}

	@Override
	public Long getParentEventId(Long translationId) {

		
		return repo.getEnclosingEventId(translationId);
	}


}
