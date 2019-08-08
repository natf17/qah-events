package com.qah.kiosk.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.domain.EventTranslationObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
@TestPropertySource(locations="classpath:test-application.properties")
@AutoConfigureMockMvc
public class LiveSecurityTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	private final String badJwtValue = "labvaubliraer9834qh";

	
	/*
	 * GET should not require a JWT
	 */
	@Test
	public void get_returnsOK() throws Exception{
		mockMvc.perform(get("/events?eventLang=en&eventType=CA-CO&after=2020-03-27&before=2020-03-30"))
			   .andExpect(status().isOk());
					
	}
	
	/*
	 * POST should require a JWT
	 */
	@Test
	public void postWithoutJWT_returns401() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		mockMvc.perform(post("/translations/event/4")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(eto)))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}
	
	/*
	 * POST should fail given invalid JWT 
	 */
	@Test
	public void postWithValidJWT_returns202() throws Exception{
		EventTranslationObject eto = new EventTranslationObject();
		eto.setId(null);
		eto.setEventTitle("Ser decidido");
		eto.setEventLanguage("en");
		eto.setDataLanguage("pr");
		eto.setComments("Comentarios");
		
		mockMvc.perform(post("/translations/event/4")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(eto))
						.header("Authorization", "Bearer " + this.badJwtValue))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

}
