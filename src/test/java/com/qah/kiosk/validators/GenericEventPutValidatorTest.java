package com.qah.kiosk.validators;

import static org.mockito.Mockito.when;

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
import com.qah.kiosk.service.EventService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={ MainApp.class})
public class GenericEventPutValidatorTest {

	@Autowired
	private GenericEventPutValidator validator;
	
	@MockBean
	private EventService service;
	
	@MockBean
	private GenericEventPartialValidator partialValidator;
	
	@Test
	public void givenNullId_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(null);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("id"));
		
	}
	
	@Test
	public void givenNonNullDefaultTranslationId_thenValidate_logsFieldError() {
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
	
	@Test
	public void givenIdForNonExistingEvent_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(null);
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasGlobalErrors());
		
	}
	
	@Test
	public void givenIdForExistingEvent_thenValidate_noError() {
		GenericEvent repoEvent = new GenericEvent();
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(repoEvent);
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasGlobalErrors());
		
	}
	
	
	@Test
	public void givenIdInTranslation_thenValidate_logsFieldError() {
		GenericEvent repoEvent = new GenericEvent();
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		
		ev.setEventTranslations(trs);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(repoEvent);
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTranslations[0].id"));
		
	}
	
	@Test
	public void givenMissingTitleInTranslation_thenValidate_logsFieldError() {
		GenericEvent repoEvent = new GenericEvent();
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle(null);
		eto.setId(null);
		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		
		ev.setEventTranslations(trs);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(repoEvent);
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTranslations[0].eventTitle"));
		
	}
	
	@Test
	public void givenDuplicateTranslation_thenValidate_logsFieldError() {
		GenericEvent repoEvent = new GenericEvent();
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("title");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setComments("Comments");
		eto2.setDataLanguage("es");
		eto2.setEventLanguage("ing");
		eto2.setEventTitle("titulo");
		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		trs.add(eto2);
		
		ev.setEventTranslations(trs);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(repoEvent);
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTranslations[1]"));
		
	}
	
	@Test
	public void givenTranslationSameAsDefault_thenValidate_logsFieldError() {
		GenericEvent repoEvent = new GenericEvent();
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("en");
		eto.setEventLanguage("en");
		eto.setEventTitle("title");

		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		
		ev.setEventTranslations(trs);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(repoEvent);
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTranslations[0]"));
		
	}
	
	@Test
	public void givenValidEvent_thenValidate_noError() {
		GenericEvent repoEvent = new GenericEvent();
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setEventType("CA-CO");
		ev.setId(2L);
		
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("ko");
		eto.setEventLanguage("en");
		eto.setEventTitle("title");
		
		EventTranslationObject eto2 = new EventTranslationObject();
		eto2.setComments("Comments");
		eto2.setDataLanguage("es");
		eto2.setEventLanguage("ing");
		eto2.setEventTitle("titulo");

		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		trs.add(eto2);
		ev.setEventTranslations(trs);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		when(service.getEvent(2L)).thenReturn(repoEvent);
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasErrors());
		
	}
}
