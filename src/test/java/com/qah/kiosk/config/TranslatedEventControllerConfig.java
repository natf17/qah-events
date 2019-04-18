package com.qah.kiosk.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.qah.kiosk.service.EventService;

@Configuration
@Profile("test5")
public class TranslatedEventControllerConfig {

	@Bean
	@Primary
	public EventService eventService() {
		return mock(EventService.class);
	}
}
