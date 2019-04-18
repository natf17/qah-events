package com.qah.kiosk.repository.impl.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.EventRepository;
import com.qah.kiosk.repository.TranslationRepository;

@Lazy
@Repository
public class DefaultEventRepository implements EventRepository {
		
	private JdbcTemplate jdbcTemplate;
	
	private EventRowMapper eventRowMapper;
	
	private TranslationRepository translationRepo;
	
	private static final String INSERT_EVENT = "INSERT INTO Events(eventType, defaultDataLang, eventStartDate, eventEndDate) VALUES (?, ?, ?, ?)";

	private static final String GET_EVENT_WITH_ID = "SELECT * FROM EVENTS WHERE id = ?";
	
	private static final String PUT_EVENT = "UPDATE Events SET eventType = ?, defaultDataLang = ?, eventStartDate = ?, eventEndDate = ? WHERE id = ?";

	private static final String DELETE_EVENT_BY_ID	 = "DELETE FROM EVENTS WHERE id = ?";

	
	@Autowired
	private void setTranslationRepository(TranslationRepository translationRepo) {
		this.translationRepo = translationRepo;
	}
	
	@Autowired
	private void setEventRowMapper(EventRowMapper eventRowMapper) {
		this.eventRowMapper = eventRowMapper;
	}
	
	
	@Autowired
	private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/*
	 * Returns null if no object found
	 */
	@Override
	public Event getEvent(Long id) {
		EventPiece eventPiece = null;
		try {
			eventPiece = jdbcTemplate.queryForObject(GET_EVENT_WITH_ID, eventRowMapper, id);
		} catch(EmptyResultDataAccessException ex) {
			return null;
		}
		if(eventPiece == null) {
			// should never get to this point...
			return null;
		}

		List<EventTranslation> eventTranslations = translationRepo.getEventTranslationsForEventId(eventPiece.getId());

		return Event.createEvent(eventPiece, eventTranslations);
	}

	@Override
	public Event postEvent(Event event) {
		
		// create the event
		
		// the keyHolder contains the auto-generated id
		KeyHolder holder = new GeneratedKeyHolder();
				
		jdbcTemplate.update( conn -> {
			PreparedStatement statement = conn.prepareStatement(INSERT_EVENT, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, event.getEventType());
			statement.setString(2, event.getDefaultDataLang());
			statement.setString(3, event.getEventStartDate().toString());
			statement.setString(4, event.getEventEndDate().toString());
			
			return statement;
		}, holder);
				
		Long eventId = Long.valueOf(holder.getKey().longValue());

		List<EventTranslation> translations = event.getEventTranslations();
		
		if(translations == null) {
			translations = new ArrayList<>();
		}

		translations.forEach(f -> {
			translationRepo.saveTranslation(eventId, f);

		});
						
		event.setId(eventId);
		
		return event;
		
	}
	
	

	@Override
	public Event putEvent(Event event) {
		jdbcTemplate.update(PUT_EVENT, event.getEventType().toString(), event.getDefaultDataLang(), event.getEventStartDate().toString(), event.getEventEndDate().toString(), event.getId());
		
		// update translations by deleting and rewriting
		List<EventTranslation> translationsDeleted = translationRepo.getEventTranslationsForEventId(event.getId());
		translationsDeleted.forEach(t -> translationRepo.deleteTranslation(t.getId()));
		
		List<EventTranslation> translationsUpdated = event.getEventTranslations();
		List<EventTranslation> translationsNew = new ArrayList<>();
		translationsUpdated.forEach(f -> {
			translationsNew.add(translationRepo.saveTranslation(event.getId(), f));

		});
		
		event.setEventTranslations(translationsNew);
		
		return event;
	}

	@Override
	public int deleteEvent(Long id) {
		return jdbcTemplate.update(DELETE_EVENT_BY_ID, id);
		
	}

}
