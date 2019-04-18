package com.qah.kiosk.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.qah.kiosk.service.EventsService;

@Configuration
@Profile("test6")
public class TranslatedEventsControllerConfig {

	@Bean
	@Primary
	public EventsService eventService() {
		return mock(EventsService.class);
	}
}
