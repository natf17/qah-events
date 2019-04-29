package com.qah.kiosk.util.converters;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.qah.kiosk.util.EventType;

@Component
public class StringToEventType implements Converter<String, EventType>{

	@Override
	public EventType convert(String e1) {		
		
		if(e1 == null || e1.isEmpty()) {
			return null;
		}

		return new EventType(e1);
		
	}


}
