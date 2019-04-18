package com.qah.kiosk.validators;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.domain.GenericEvent;
import com.qah.kiosk.service.EventsService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={MainApp.class})
public class GenericEventFullValidatorTest {
	
	@Autowired
	private GenericEventFullValidator validator;
	
	@MockBean
	private EventsService eventsService;
	
	@MockBean
	private GenericEventPartialValidator partialValidator;
	
	@Test
	public void givenNonNullId_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		doReturn(null).when(eventsService).getGenericEventsStrictlyOn(any(), any());
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("id"));
		
	}
	
	@Test
	public void givenNonNulldefaultTranslationId_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
				
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("defaultTranslationId"));
		
	}
	
	// see EventValidatorUtilityTest for testing duplicate events in repository
	
	// see EventValidatorUtilityTest for exhaustive testing of duplicate translations
	
	@Test
	public void givenDuplicateTranslations_thenValidate_logsFieldError() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(709L);
		ev1.setEventType("CA-BR");
		ev1.setEventStartDate(eventStartDate);
		ev1.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("en");
		eto.setEventLanguage("ing");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev1.setEventTranslations(etos);
		
		Errors errors = new BeanPropertyBindingResult(ev1, "event");

		validator.validate(ev1, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTranslations[0]"));
		
		
	}
	
	@Test
	public void givenValidEvent_thenValidate_noError() {
		LocalDate eventStartDate = LocalDate.of(2019, 03, 25);
		LocalDate eventEndDate = LocalDate.of(2019, 03, 30);
		
		GenericEvent ev1 = new GenericEvent();
		ev1.setDefaultDataLang("es");
		ev1.setEventTranslations(new ArrayList<>());
		ev1.setComments("Comments");
		ev1.setDataLanguage("en");
		ev1.setEventLanguage("en");
		ev1.setEventTitle("Title");
		ev1.setDefaultTranslationId(null);
		ev1.setEventType("CA-BR");
		ev1.setEventStartDate(eventStartDate);
		ev1.setEventEndDate(eventEndDate);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setDataLanguage("sps");
		eto.setEventLanguage("ing");
		eto.setEventTitle("other");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		ev1.setEventTranslations(etos);
		
		Errors errors = new BeanPropertyBindingResult(ev1, "event");

		validator.validate(ev1, errors);
		
		Assert.assertFalse(errors.hasErrors());
		
		
	}
	
	
}
