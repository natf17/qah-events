package com.qah.kiosk.repository.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.TranslationRepository;

@Lazy
@Repository
public class DefaultTranslationRepository implements TranslationRepository {

	private JdbcTemplate jdbcTemplate;
	
	
	private static final String GET_TRANSLATION_IDS_FOR_EVENT_ID = "SELECT * FROM EventTranslations WHERE Events = ?";
	
	private static final String GET_EVENT_TRANSLATION_BY_ID = "SELECT * FROM EventTranslations WHERE id = ?";
	
	private static final String INSERT_TRANSLATION = "INSERT INTO EventTranslations(dataLang, eventLang, eventTitle, comments, Events) VALUES (?, ?, ?, ?, ?)";

	private static final String DELETE_TRANSLATION = "DELETE FROM EventTranslations WHERE id = ?";
	
	private static final String PUT_TRANSLATION = "UPDATE EventTranslations SET dataLang = ?, eventLang = ?, eventTitle = ?, comments = ? WHERE id = ?";

	private static final String GET_EVENT_ID_MATCH_TRANSLATION_ID = "SELECT Events FROM EventTranslations WHERE id = ?";

	@Autowired
	private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	@Override
	public List<EventTranslation> getEventTranslationsForEventId(Long id) {

		List<EventTranslation> translations = jdbcTemplate.query(GET_TRANSLATION_IDS_FOR_EVENT_ID, new EventTranslationRowMapper(), id);
		
		if(translations == null) {
			return Collections.emptyList();
		}

		return translations;
	}
	
	/*
	 * Returns null if no object is found
	 */
	
	@Override
	public EventTranslation saveTranslation(Long eventId, EventTranslation tr) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long id = null;
		jdbcTemplate.update(conn -> {
			PreparedStatement statement = conn.prepareStatement(INSERT_TRANSLATION,  Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, tr.getDataLanguage());
			statement.setString(2, tr.getEventLanguage());
			statement.setString(3, tr.getEventTitle());
			statement.setString(4, tr.getComments());
			statement.setLong(5, eventId);
			
			
			return statement;
		}, holder);		
		
		id = Long.valueOf(holder.getKey().longValue());
		
		tr.setId(id);
		
		return tr;
		
	}

	@Override
	public int deleteTranslation(Long id) {
		return jdbcTemplate.update(DELETE_TRANSLATION, id);

	}


	@Override
	public EventTranslation getTranslation(Long translationId) {
		EventTranslation translation = null;
		try {
			translation = jdbcTemplate.queryForObject(GET_EVENT_TRANSLATION_BY_ID, new EventTranslationRowMapper(), translationId);
		} catch(EmptyResultDataAccessException ex) {
			return null;
		}
		
		return translation;
	}


	@Override
	public EventTranslation putTranslation(EventTranslation tr) {
		jdbcTemplate.update(PUT_TRANSLATION, tr.getDataLanguage(), tr.getEventLanguage(), tr.getEventTitle(), tr.getComments(), tr.getId());
		
		return tr;
	}
	
	@Override
	public Long getEnclosingEventId(Long translationId) {
		
		Long eventId = jdbcTemplate.queryForObject(GET_EVENT_ID_MATCH_TRANSLATION_ID, new Object[]{translationId}, Long.class);
		
		return eventId;
		
	}

}
