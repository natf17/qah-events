package com.qah.kiosk.lang;

import java.util.List;

import com.qah.kiosk.entity.EventTranslation;

public class EventTranslationUtility {
	

	public static EventTranslation getDefault(List<EventTranslation> translations, String defaultDataLang) {
	
		for(EventTranslation translation : translations) {
			if(defaultDataLang.equals(translation.getDataLanguage())) {
				return translation;
			}
		}
		
		return null;
	}
}
