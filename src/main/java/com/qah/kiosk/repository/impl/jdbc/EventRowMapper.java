package com.qah.kiosk.repository.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.qah.kiosk.util.converters.StringToLocalDateConverter;

@Component
public class EventRowMapper implements RowMapper<EventPiece> {

	private StringToLocalDateConverter dateConverter;
	
	@Autowired
	private void setStringToLocalDateConverter(StringToLocalDateConverter dateConverter) {
		System.out.println(dateConverter);
		this.dateConverter = dateConverter;
	}
	
	
	@Override
	public EventPiece mapRow(ResultSet rs, int rowNum) throws SQLException {
		EventPiece eventSkeleton = new EventPiece();
		String eventType = rs.getString("eventType");
	
		eventSkeleton.setDefaultDataLang(rs.getString("defaultDataLang"));
		eventSkeleton.setId(rs.getLong("id"));
		eventSkeleton.setEventStartDate(dateConverter.convert(rs.getString("eventStartDate")));
		eventSkeleton.setEventEndDate(dateConverter.convert(rs.getString("eventEndDate")));
		
		
		eventSkeleton.setEventType(eventType);
		
		return eventSkeleton;
	}
	
}