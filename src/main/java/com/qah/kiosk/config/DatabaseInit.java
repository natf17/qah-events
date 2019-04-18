package com.qah.kiosk.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.qah.kiosk.entity.Event;
import com.qah.kiosk.entity.EventTranslation;
import com.qah.kiosk.repository.EventsRepository;

@Component
public class DatabaseInit implements ApplicationListener<ContextRefreshedEvent> {
	
	private EventsRepository eventsRepository;

	
	@Autowired
	public void setEventsRespository(EventsRepository repo) {
		this.eventsRepository = repo;
	}



	@Override
	public void onApplicationEvent(ContextRefreshedEvent cre) {
		
		// event1
		Event event1 = new Event();
		event1.setEventType("REG");
		event1.setDefaultDataLang("en");
		event1.setEventStartDate(LocalDate.of(2018, 7, 20));
		event1.setEventEndDate(LocalDate.of(2018, 7, 22));
		event1.setDefaultDataLang("en");
		
		// event2
		Event event2 = new Event();
		event2.setEventType("REG");
		event2.setDefaultDataLang("en");
		event2.setEventStartDate(LocalDate.of(2018, 7, 6));
		event2.setEventEndDate(LocalDate.of(2018, 7, 8));
		event2.setDefaultDataLang("en");
		
		// event3
		Event event3 = new Event();
		event3.setEventType("REG");
		event3.setDefaultDataLang("en");
		event3.setEventStartDate(LocalDate.of(2018, 7, 14));
		event3.setEventEndDate(LocalDate.of(2018, 7, 16));
		event3.setDefaultDataLang("en");
		
		
		List<EventTranslation> translations = new ArrayList<>();
		
		EventTranslation translation1 = new EventTranslation();
		translation1.setDataLanguage("sp");
		translation1.setEventLanguage("ing");
		translation1.setEventTitle("Sea valiente!");
		translation1.setComments("Estos son algunos comentarios.");
		
		EventTranslation translation2 = new EventTranslation();
		translation2.setDataLanguage("fr");
		translation2.setEventLanguage("angl");
		translation2.setEventTitle("Sois courageux");
		translation2.setComments("Voici quelques commentaires.");
		
		EventTranslation translation3 = new EventTranslation();
		translation3.setDataLanguage("en");
		translation3.setEventLanguage("eng");
		translation3.setEventTitle("Be courageous!");
		translation3.setComments("These are comments.");
		
		
		translations.add(translation1);
		translations.add(translation2);
		translations.add(translation3);
		
		
		event1.setEventTranslations(translations);
		event2.setEventTranslations(translations);
		event3.setEventTranslations(translations);



		eventsRepository.saveEventsList(Arrays.asList(event1, event2, event3));
		
	}

}
