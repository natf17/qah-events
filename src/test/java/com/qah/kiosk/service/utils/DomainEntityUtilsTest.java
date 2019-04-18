package com.qah.kiosk.service.utils;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;


public class DomainEntityUtilsTest {

	@Test
	public void givenGeneric_thenGenericToEvent_ReturnsEvent_testTranslation() {
		// Sample GenericEvent
		GenericEvent ge = new GenericEvent();
		ge.setComments("Comments");
		ge.setDataLanguage("sample");
		ge.setDefaultDataLang("defaultDataLanguage");
		ge.setDefaultTranslationId(29L);
		ge.setEventEndDate(LocalDate.of(2019, 3, 28));
		ge.setEventEndDate(LocalDate.of(2019, 3, 25));
		ge.setEventLanguage("eventLanguage");
		ge.setEventTitle("title");
		ge.setEventType("CA-CO");
		ge.setId(34L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("newLangComment");
		eto.setDataLanguage("EN");
		eto.setEventLanguage("evLang");
		eto.setEventTitle("translatedTitle");
		eto.setId(45L);
		
		ge.setEventTranslations(Arrays.asList(eto));
		
		
		Event event = DomainEntityUtils.genericToEvent(ge);
		
		Assert.assertEquals(2, event.getEventTranslations().size());
	}
	
	@Test
	public void givenGeneric_thenGenericToEvent_ReturnsEvent_testDefaultDataLang() {
		// Sample GenericEvent
		GenericEvent ge = new GenericEvent();
		ge.setComments("Comments");
		ge.setDataLanguage("sample");
		ge.setDefaultDataLang("defaultDataLanguage");
		ge.setDefaultTranslationId(29L);
		ge.setEventEndDate(LocalDate.of(2019, 3, 28));
		ge.setEventEndDate(LocalDate.of(2019, 3, 25));
		ge.setEventLanguage("eventLanguage");
		ge.setEventTitle("title");
		ge.setEventType("CA-CO");
		ge.setId(34L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("newLangComment");
		eto.setDataLanguage("EN");
		eto.setEventLanguage("evLang");
		eto.setEventTitle("translatedTitle");
		eto.setId(45L);
		
		ge.setEventTranslations(Arrays.asList(eto));
		
		
		Event event = DomainEntityUtils.genericToEvent(ge);
		
		Assert.assertEquals("defaultDataLanguage", event.getDefaultDataLang());
	}
	
	@Test
	public void givenEvent_thenEventToGeneric_ReturnsGeneric_testTranslation() {
		
		// Sample Event
		Event eve = new Event();
		eve.setDefaultDataLang("defaultDataLanguage");
		eve.setEventEndDate(LocalDate.of(2019, 3, 28));
		eve.setEventEndDate(LocalDate.of(2019, 3, 25));
		eve.setEventType("CA-CO");
		eve.setId(34L);

		EventTranslation et = new EventTranslation();
		et.setComments("Comments");
		et.setDataLanguage("sample");
		et.setEventLanguage("eventLanguage");
		et.setEventTitle("title");
		et.setId(5L);
		
		EventTranslation et2 = new EventTranslation();
		et2.setComments("Comentarios");
		et2.setDataLanguage("defaultDataLanguage");
		et2.setEventLanguage("eventLanguage2");
		et2.setEventTitle("title2");
		et2.setId(6L);

		
		eve.setEventTranslations(Arrays.asList(et, et2));
		
		GenericEvent ge = DomainEntityUtils.eventToGeneric(eve);
				
		Assert.assertEquals(1, ge.getEventTranslations().size());
		Assert.assertEquals("defaultDataLanguage", ge.getDataLanguage());
		Assert.assertEquals("defaultDataLanguage", ge.getDefaultDataLang());
		Assert.assertEquals("sample", ge.getEventTranslations().get(0).getDataLanguage());
		
	}
}
