package com.qah.kiosk.repository.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.qah.kiosk.entity.EventTranslation;

public class EventTranslationRowMapper implements RowMapper<EventTranslation> {

	@Override
	public EventTranslation mapRow(ResultSet rs, int rowNum) throws SQLException {
		EventTranslation translation = new EventTranslation();
		
		translation.setEventTitle(rs.getString("eventTitle"));
		translation.setEventLanguage(rs.getString("eventLang"));
		translation.setDataLanguage(rs.getString("dataLang"));
		translation.setComments(rs.getString("comments"));
		translation.setId(rs.getLong("id"));
		
		return translation;
	}
	
}
