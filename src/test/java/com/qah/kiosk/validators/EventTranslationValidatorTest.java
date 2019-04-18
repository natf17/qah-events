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
import com.qah.kiosk.service.TranslationsService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={ MainApp.class})
public class EventTranslationValidatorTest {
	
	@Autowired
	private EventTranslationValidator validator;
	
	@MockBean
	private TranslationsService service;
	
	@MockBean
	private EventService eventService;
	
	@Test
	public void givenNoId_thenPut_returnsFieldError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("id"));

	}
	
	@Test
	public void givenNonExistingId_thenPut_returnsFieldError() {
		when(service.getTranslation(709L)).thenReturn(null);

		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("id"));

	}
	
	@Test
	public void givenValidId_thenPut_returnsNoFieldError() {
		
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		when(service.getTranslation(709L)).thenReturn(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("es");
		ev.setEventTranslations(new ArrayList<>());
		ev.setComments("Comments");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(709L);
		ev.setEventType("CA-CO");
		
		when(eventService.getEnclosingEvent(709L)).thenReturn(ev);

		eto.setEventLanguage("newLang");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("id"));

	}
	
	
	@Test
	public void ifDataLanguageIsDefault_thenPutItCannotBeChanged() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		when(service.getTranslation(709L)).thenReturn(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("es");
		ev.setComments("Comments");
		ev.setDataLanguage("es");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(709L);
		ev.setEventType("CA-CO");
				
		ev.setEventTranslations(new ArrayList<>());
		
		when(eventService.getEnclosingEvent(709L)).thenReturn(ev);

		// we're changing the dataLanguage although existing object's language is the defaultDataLanguage
		eto.setDataLanguage("newLang");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("dataLanguage"));
	}

	@Test
	public void ifDataLanguageIsNotDefault_thenPutItCanBeChanged() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		when(service.getTranslation(709L)).thenReturn(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		
		ev.setEventTranslations(trs);
		
		when(eventService.getEnclosingEvent(709L)).thenReturn(ev);

		// we're changing the dataLanguage of a translation that is not the event's defaultDataLanguage
		eto.setDataLanguage("newLang");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("dataLanguage"));
	}
	
	@Test
	public void ifDuplicate_thenPutFieldError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		when(service.getTranslation(709L)).thenReturn(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		
		ev.setEventTranslations(trs);
		
		when(eventService.getEnclosingEvent(709L)).thenReturn(ev);

		// we're changing the dataLanguage of a translation to force a duplicate
		eto.setDataLanguage("en");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertTrue(errors.hasGlobalErrors());
	}
	
	@Test
	public void ifValid_thenPutNoErrors() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(709L);
		
		when(service.getTranslation(709L)).thenReturn(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		
		List<EventTranslationObject> trs = new ArrayList<>();
		trs.add(eto);
		
		ev.setEventTranslations(trs);
		
		when(eventService.getEnclosingEvent(709L)).thenReturn(ev);

		// we're changing the dataLanguage of a translation to force a duplicate
		eto.setEventLanguage("en");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, errors);
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	@Test
	public void givenNoEventId_thenPost_returnsGlobalError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, null, errors);
		
		Assert.assertTrue(errors.hasGlobalErrors());

	}
	
	@Test
	public void givenNonExistingEventId_thenPost_returnsGlobalError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, 1000L, errors);
		
		Assert.assertTrue(errors.hasGlobalErrors());

	}
	
	@Test
	public void givenNonNullId_thenPost_returnsFieldError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		eto.setId(4L);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		when(eventService.getEvent(710L)).thenReturn(ev);

		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, 710L, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("id"));

	}
	
	@Test
	public void givenDuplicate_thenPost_returnsGlobalError() {
		EventTranslationObject eto = new EventTranslationObject();
		
		eto.setComments("Comments");
		eto.setDataLanguage("en");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		when(eventService.getEvent(710L)).thenReturn(ev);

		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(eto, 710L, errors);
		
		Assert.assertTrue(errors.hasGlobalErrors());

	}
	
	@Test
	public void givenDuplicateTranslation_thenPost_returnsGlobalError() {
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("es");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		ev.setEventTranslations(etos);
		
		when(eventService.getEvent(710L)).thenReturn(ev);

		EventTranslationObject newEto = new EventTranslationObject();
		newEto.setComments("Comments");
		newEto.setDataLanguage("es");
		newEto.setEventLanguage("en");
		newEto.setEventTitle("Title");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(newEto, 710L, errors);

		Assert.assertTrue(errors.hasGlobalErrors());

	}
	
	@Test
	public void givenValidTranslation_thenPost_returnsNoError() {
		EventTranslationObject eto = new EventTranslationObject();
		eto.setComments("Comments");
		eto.setDataLanguage("ko");
		eto.setEventLanguage("en");
		eto.setEventTitle("Title");
		
		List<EventTranslationObject> etos = new ArrayList<>();
		etos.add(eto);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		ev.setEventTranslations(etos);
		
		when(eventService.getEvent(710L)).thenReturn(ev);

		EventTranslationObject newEto = new EventTranslationObject();
		newEto.setComments("Comments");
		newEto.setDataLanguage("es");
		newEto.setEventLanguage("en");
		newEto.setEventTitle("Title");
		
		Errors errors = new BeanPropertyBindingResult(eto, "eventTranslationObject");

		validator.validate(newEto, 710L, errors);

		Assert.assertFalse(errors.hasErrors());

	}

}
