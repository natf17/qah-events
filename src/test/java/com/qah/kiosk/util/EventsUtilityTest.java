package com.qah.kiosk.util;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.domain.SingleTranslatedEvent;


public class EventsUtilityTest {
	
	@Test
	public void transformGenericToSingleTranslatedEvent_ReturnsEvent() {
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
		
		SingleTranslatedEvent ste = EventsUtility.transformGenericToSingleTranslatedEvent(ge, "EN");
		
		Assert.assertEquals(new Long(45L), ste.getCurrentTranslationId());
		Assert.assertEquals("defaultDataLanguage", ste.getDefaultDataLang());
		
	}

	
}
