package com.qah.kiosk.repository.impl.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.qah.kiosk.repository.EventTypeRepository;
import com.qah.kiosk.util.EventType;

/*
 * Disabled for now.
 */
public class EventTypeRepositoryImpl implements EventTypeRepository {
	
	private static final String GET_EVENT_TYPES = "SELECT * FROM EVENT_TYPES";

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<EventType> getEventTypes() {
		List<EventType> eventTypes;
		eventTypes = jdbcTemplate.query(GET_EVENT_TYPES, (i, j) -> {
			return new EventType(i.getString("abbr"), i.getString("name"));
		});
		
		if(eventTypes == null || eventTypes.isEmpty()) {
			return null;
		}

		return eventTypes;
	}

}
