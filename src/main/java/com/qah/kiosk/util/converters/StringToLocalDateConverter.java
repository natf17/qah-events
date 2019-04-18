package com.qah.kiosk.util.converters;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate>{

	@Override
	public LocalDate convert(String date) {
		String da = date.trim();
		
		// expects a date in 'YYYY-MM-DD' format
		
		String[] pieces = da.split("-");
		
		LocalDate finalDate = LocalDate.of(Integer.parseInt(pieces[0]), Integer.parseInt(pieces[1]), Integer.parseInt(pieces[2]));
		
		return finalDate;
	}

}
