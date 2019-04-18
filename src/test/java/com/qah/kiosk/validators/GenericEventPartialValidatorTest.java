package com.qah.kiosk.validators;

import java.time.LocalDate;

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
import com.qah.kiosk.domain.GenericEvent;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={ MainApp.class})
public class GenericEventPartialValidatorTest {

	@Autowired
	private GenericEventPartialValidator validator;
	
	@MockBean
	private EventTypeValidator eventTypeValidator;
	
	@Test
	public void givenNoEventType_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle("TitleOther");
		ev.setDefaultTranslationId(710L);
		ev.setEventType(null);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventType"));
		
		
	}
	
	@Test
	public void givenNullEventTitle_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage("en");
		ev.setEventTitle(null);
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventTitle"));
		
		
	}
	
	@Test
	public void givenNullEventLanguage_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("en");
		ev.setEventLanguage(null);
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventLanguage"));
		
		
	}
	
	@Test
	public void givenNullDataLanguageAndDefaultDataLanguage_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang(null);
		ev.setComments("CommentsOther");
		ev.setDataLanguage(null);
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("defaultDataLang"));
		
		
	}
	
	@Test
	public void givenNullDataLanguageAndNonNullDefaultDataLanguage_thenValidate_noError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("en");
		ev.setComments("CommentsOther");
		ev.setDataLanguage(null);
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("defaultDataLang"));
		Assert.assertFalse(errors.hasFieldErrors("dataLanguage"));
		
		
	}
	
	@Test
	public void givenInconsistentDataLanguageAndDefaultDataLanguage_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language2");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("language1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("defaultDataLang"));
		
		
	}
	
	@Test
	public void givenNonNullDataLanguageAndNullDefaultDataLanguage_thenValidate_noError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang(null);
		ev.setComments("CommentsOther");
		ev.setDataLanguage("language1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("defaultDataLang"));
		
		
	}
	
	@Test
	public void givenNonNullDataLanguageAndNullDefaultDataLanguage_thenValidate_setsDefaultDataLanguage() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang(null);
		ev.setComments("CommentsOther");
		ev.setDataLanguage("language1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertEquals("language1", ev.getDefaultDataLang());
		
		
	}
	
	@Test
	public void giveNullDataLanguageAndNonNullDefaultDataLanguage_thenValidate_noError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage(null);
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("defaultDataLang"));
		
		
	}
	
	@Test
	public void giveNullDataLanguageAndNonNullDefaultDataLanguage_thenValidate_setsDataLanguage() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage(null);
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertEquals("language1", ev.getDataLanguage());
		
	}
	
	@Test
	public void givenNullstartDate_thenValidate_logsFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("lang1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		ev.setEventStartDate(null);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventStartDate"));
		
		
	}
	
	@Test
	public void givenNonNullStartDateAndNullEndDate_thenValidate_noFieldError() {
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("lang1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		ev.setEventStartDate(LocalDate.of(2019, 4, 3));
		ev.setEventEndDate(null);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasFieldErrors("eventEndDate"));
		
	}
	
	@Test
	public void givenNonNullStartDateAndNullEndDate_thenValidate_setsEndDate() {
		LocalDate startDate = LocalDate.of(2019, 4, 3);
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("lang1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		ev.setEventEndDate(null);
		ev.setEventStartDate(startDate);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertEquals(startDate, ev.getEventEndDate());
		
	}
	
	@Test
	public void givenEndDateBeforeStartDate_thenValidate_logsFieldError() {
		LocalDate startDate = LocalDate.of(2019, 4, 3);
		LocalDate endDate = startDate.minusDays(5);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("lang1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		ev.setEventStartDate(startDate);
		ev.setEventEndDate(endDate);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("eventEndDate"));
		
	}
	
	@Test
	public void givenvalidEvent_thenValidate_logsNoError() {
		LocalDate startDate = LocalDate.of(2019, 4, 3);
		LocalDate endDate = startDate.plusDays(5);
		
		GenericEvent ev = new GenericEvent();
		ev.setDefaultDataLang("language1");
		ev.setComments("CommentsOther");
		ev.setDataLanguage("language1");
		ev.setEventLanguage("en");
		ev.setEventTitle("Title");
		ev.setDefaultTranslationId(710L);
		ev.setEventType("CA-CO");
		ev.setEventStartDate(startDate);
		ev.setEventEndDate(endDate);
		
		Errors errors = new BeanPropertyBindingResult(ev, "event");
		
		validator.validate(ev, errors);
		
		Assert.assertFalse(errors.hasErrors());
		
	}
	
	
}
