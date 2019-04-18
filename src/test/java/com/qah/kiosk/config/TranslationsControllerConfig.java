package com.qah.kiosk.config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.qah.kiosk.service.TranslationsService;
import com.qah.kiosk.validators.EventTranslationValidator;

@Configuration
@Profile("test7")
public class TranslationsControllerConfig {
	@Bean
    @Primary
    public TranslationsService translationsService() {
        return mock(TranslationsService.class);
    }
	
	@Bean
    @Primary
    public EventTranslationValidator translationValidator() {
		EventTranslationValidator validator = mock(EventTranslationValidator.class);
		doReturn(true).when(validator).supports(any());
		
        return validator;
    }
}
