package com.qah.kiosk.util.converters;


import org.springframework.core.convert.converter.Converter;

import com.qah.kiosk.exception.EventTypeBindingException;
import com.qah.kiosk.util.EventType;

public class StringToEventType implements Converter<String, EventType>{

	@Override
	public EventType convert(String e1) {
		EventType type = null;
		
		try {
			type = EventType.valueOf(e1.toUpperCase());
		} catch(IllegalArgumentException ex) {
			throw new EventTypeBindingException("The value [" + e1 + "] is not a valid EnumType");
		}

		return type;
	}

}
