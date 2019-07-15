package com.qah.kiosk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;
import com.qah.kiosk.security.WithMockJwtAuthenticationToken;
import com.qah.kiosk.service.TranslationsService;
import com.qah.kiosk.validators.EventTranslationValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = {TranslationsController.class})
@ContextConfiguration(classes={MainApp.class}) // see SpringBootTestContextBootstrapper
public class TranslationsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TranslationsService translationsService;
	
	@MockBean
	private JwtDecoder jwtDecoder;
	
	@MockBean
	private EventTranslationValidator translationValidator;
	
	private EventTranslationObject eto;
	
	private final String JSON_TRANSLATION = "{\"id\" : 43,"
												+ "\"eventTitle\" : \"Title\","
												+ "\"eventLanguage\" : \"sp\","
												+ "\"dataLanguage\" : \"en\","
												+ "\"comments\" : \"comments\""
												+ "}";
	
	@Before
	public void setup() {
		eto = new EventTranslationObject();
		eto.setComments("comments");
		eto.setDataLanguage("en");
		eto.setEventLanguage("sp");
		eto.setEventTitle("Title");
		eto.setId(43L);
		
		doReturn(true).when(translationValidator).supports(any());
		
	}
	
	/*
	 * Valid id...
	 * 
	 * Expect: 200 and body
	 */
	@Test
	public void givenValidId_thenGetTranslationById_returnsFullyPopulatedResponse() throws Exception {
		
		when(translationsService.getTranslation(43L)).thenReturn(eto);
		
		mockMvc.perform(get("/translations/43"))
						.andExpect(status().isOk())
						.andExpect(content()
										.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.id").value(43))
						.andExpect(jsonPath("$.comments").value("comments"))
						.andExpect(jsonPath("$.dataLanguage").value("en"))
						.andExpect(jsonPath("$.eventLanguage").value("sp"))
						.andExpect(jsonPath("$.eventTitle").value("Title"));
	}
	
	/*
	 * Invalid id...
	 * 
	 * Expect: 404 and no content
	 */
	@Test
	public void givenInvalidId_thenGetTranslationById_returns404() throws Exception {
		Long invalid = -30000L;
		when(translationsService.getTranslation(invalid)).thenReturn(null);
		
		mockMvc.perform(get("/translations/" + invalid)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound())
						.andExpect(content().string(""));
	}
	
	/*
	 * Translation received
	 * 
	 * Expect: 201 and body if successful
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidTranslation_thenPostTranslation_objectReceived() throws Exception {
		
		mockMvc.perform(post("/translations/event/43")
					.contentType(MediaType.APPLICATION_JSON)
					.content(JSON_TRANSLATION))
					.andExpect(status().isCreated());
		
		ArgumentCaptor<EventTranslationObject> etoCapt = ArgumentCaptor.forClass(EventTranslationObject.class);
		
		verify(translationsService).postTranslation(any(), etoCapt.capture());
		
		EventTranslationObject received = etoCapt.getValue();
		
		Assert.assertTrue(received.getComments().equals(eto.getComments()));
		Assert.assertTrue(received.getDataLanguage().equals(eto.getDataLanguage()));
		Assert.assertTrue(received.getEventLanguage().equals(eto.getEventLanguage()));
		Assert.assertTrue(received.getEventTitle().equals(eto.getEventTitle()));
		Assert.assertTrue(received.getId().equals(eto.getId()));
		
	}

	/*
	 * Translation received
	 * 
	 * Expect: 204 if successful
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidTranslation_thenPutTranslation_objectReceived() throws Exception {
				
		mockMvc.perform(put("/translations/43")
					.contentType(MediaType.APPLICATION_JSON)
					.content(JSON_TRANSLATION))
					.andExpect(status().isNoContent());
		
		ArgumentCaptor<EventTranslationObject> etoCapt = ArgumentCaptor.forClass(EventTranslationObject.class);
		
		verify(translationsService).putTranslation(etoCapt.capture());
		
		EventTranslationObject received = etoCapt.getValue();
		
		Assert.assertTrue(received.getComments().equals(eto.getComments()));
		Assert.assertTrue(received.getDataLanguage().equals(eto.getDataLanguage()));
		Assert.assertTrue(received.getEventLanguage().equals(eto.getEventLanguage()));
		Assert.assertTrue(received.getEventTitle().equals(eto.getEventTitle()));
		Assert.assertTrue(received.getId().equals(eto.getId()));
		
	}	
	
	/*
	 * Delete id received
	 * 
	 * Expect: 204 if successful
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenValidId_thenDeleteTranslation_successful() throws Exception {
		doReturn(true).when(translationsService).deleteTranslation(any());

		mockMvc.perform(delete("/translations/43")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNoContent());
		
		ArgumentCaptor<Long> idCapt = ArgumentCaptor.forClass(Long.class);
		verify(translationsService).deleteTranslation(idCapt.capture());
		
		Long received = idCapt.getValue();
		
		Assert.assertTrue(received.equals(43L));
		
	}	
	
	/*
	 * Delete id received
	 * 
	 * Expect: 404 if invalid id is provided
	 */
	@Test
	@WithMockJwtAuthenticationToken
	public void givenInvalidId_thenDeleteTranslation_unsuccessful() throws Exception {
		when(translationsService.deleteTranslation(43L)).thenReturn(false);

		mockMvc.perform(delete("/translations/43")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		
		
	}

}
