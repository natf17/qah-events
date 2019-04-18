package com.qah.kiosk.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;

/*
 * This class contains utility methods that map domain objects to entity objects (and vice versa).
 */
public class DomainEntityUtils {

	public static Event genericToEvent(GenericEvent ge) {
		Event e = new Event();
		e.setDefaultDataLang(ge.getDefaultDataLang());
		e.setEventEndDate(ge.getEventEndDate());
		e.setEventStartDate(ge.getEventStartDate());
		e.setEventType(ge.getEventType());
		e.setId(ge.getId());
		
		EventTranslation et = new EventTranslation();
		et.setComments(ge.getComments());
		et.setDataLanguage(ge.getDataLanguage());
		et.setEventLanguage(ge.getEventLanguage());
		et.setEventTitle(ge.getEventTitle());
		et.setId(ge.getDefaultTranslationId());
		
		List<EventTranslation> ets = new ArrayList<>();
		ets.add(et);

		if(ge.getEventTranslations() == null) {
			e.setEventTranslations(ets);
			
			return e;
		}

		List<EventTranslation> etsSecondary = ge.getEventTranslations().stream()
													.map(i -> domainTranslationToEntity(i))
													.collect(Collectors.toList());

		
		ets.addAll(etsSecondary);
		
		e.setEventTranslations(ets);
		
		return e;
		
	}
	
	public static GenericEvent eventToGeneric(Event e) {
		GenericEvent ge = new GenericEvent();
		
		ge.setDefaultDataLang(e.getDefaultDataLang());
		ge.setEventEndDate(e.getEventEndDate());
		ge.setEventStartDate(e.getEventStartDate());
		ge.setEventType(e.getEventType());
		ge.setId(e.getId());
		
		List<EventTranslation> ets = new ArrayList<>();
		List<EventTranslation> defaultHolder = new ArrayList<>();
		
		if(e.getEventTranslations() == null) {
			// should never happen
			return ge;
		}
		e.getEventTranslations().forEach(i -> {
			if(i.getDataLanguage().equals(ge.getDefaultDataLang())) {
				defaultHolder.add(i);
			} else {
				ets.add(i);
			}
		});
		
		EventTranslation defTr = defaultHolder.get(0);
		
		if(defTr == null) {
			// Should never happen...
			// a default should exist;
		}
		
		List<EventTranslationObject> etsG = ets.stream()
													.map(i -> entityTranslationToDomain(i))
													.collect(Collectors.toList());
		
		if(defTr == null) {
			// Should never happen...
			// a default should exist;
		} else {
			ge.setComments(defTr.getComments());
			ge.setEventLanguage(defTr.getEventLanguage());
			ge.setEventTitle(defTr.getEventTitle());
			ge.setDefaultTranslationId(defTr.getId());
			ge.setDataLanguage(defTr.getDataLanguage());
		}
		
		ge.setEventTranslations(etsG);

		return ge;
		
	}
	
	public static EventTranslation domainTranslationToEntity(EventTranslationObject eto) {
		if(eto == null) {
			return null;
		}
		EventTranslation et = new EventTranslation();
		et.setComments(eto.getComments());
		et.setDataLanguage(et.getDataLanguage());
		et.setEventLanguage(eto.getEventLanguage());
		et.setEventTitle(eto.getEventTitle());
		et.setId(eto.getId());
		
		return et;
	}
	
	public static EventTranslationObject entityTranslationToDomain(EventTranslation eto) {
		if(eto == null) {
			return null;
		}
		EventTranslationObject et = new EventTranslationObject();
		et.setComments(eto.getComments());
		et.setDataLanguage(eto.getDataLanguage());
		et.setEventLanguage(eto.getEventLanguage());
		et.setEventTitle(eto.getEventTitle());
		et.setId(eto.getId());
		
		return et;
	}
	
	
	
	
}
