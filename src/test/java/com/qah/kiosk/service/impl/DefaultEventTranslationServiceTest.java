package com.qah.kiosk.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.qah.kiosk.repository.TranslationRepository;

public class DefaultEventTranslationServiceTest {

	@Test
	public void whenNoMatchingTranslation_thenGetTranslation_returnsNull() {
		DefaultEventTranslationService service = new DefaultEventTranslationService();
		Long translationId = 34L;
		
		TranslationRepository repo = mock(TranslationRepository.class);
		when(repo.getTranslation(translationId)).thenReturn(null);
		service.setTranslationRepository(repo);
		
		Assert.assertNull(service.getTranslation(translationId));
		
		
	}
	
	@Test
	public void whenNegativeTranslationId_thenGetTranslation_returnsNull() {
		DefaultEventTranslationService service = new DefaultEventTranslationService();
		Long translationId = -34L;
		
		Assert.assertNull(service.getTranslation(translationId));
		
		
	}
	
	@Test
	public void ifNoRowsAffected_thenDeleteTranslation_returnsFalse() {
		DefaultEventTranslationService service = new DefaultEventTranslationService();
		TranslationRepository repo = mock(TranslationRepository.class);
		Long id = 90L;
		when(repo.deleteTranslation(id)).thenReturn(0);
		service.setTranslationRepository(repo);
		
		Assert.assertFalse(service.deleteTranslation(90L));
		
	}
	
	@Test
	public void ifRowsAffected_thenDeleteTranslation_returnsTrue() {
		DefaultEventTranslationService service = new DefaultEventTranslationService();
		TranslationRepository repo = mock(TranslationRepository.class);
		Long id = 90L;
		when(repo.deleteTranslation(id)).thenReturn(1);
		service.setTranslationRepository(repo);
		
		Assert.assertTrue(service.deleteTranslation(90L));
		
	}
}
