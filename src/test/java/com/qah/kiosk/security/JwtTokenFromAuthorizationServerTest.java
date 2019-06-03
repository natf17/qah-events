package com.qah.kiosk.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;

/*
 * This test class creates a Jwt token as would be created by the authorization server
 * and performs tests to ascertain the default resource server security configuration
 * can parse and authenticate with the given token.
 * 
 */
public class JwtTokenFromAuthorizationServerTest {
	
	private static final String SUBJECT = "Nathan";
	private static final String CLAIM1 = "developer";
	

	@Test
	public void setup() throws Exception {

		BearerTokenAuthenticationFilter authFilter = createBearerTokenAuthenticationFilter();
		AuthenticationEntryPoint mockEntryPoint = mock(AuthenticationEntryPoint.class);
		authFilter.setAuthenticationEntryPoint(mockEntryPoint);
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		
		doReturn(getJwtHeader()).when(req).getHeader(any());
		
		authFilter.doFilter(req, mock(HttpServletResponse.class), (i,j) -> {});
		
		verify(mockEntryPoint, never()).commence(any(), any(), any());

	}
	
	
	
	
	/*
	 * Create the filter as would be created by Spring Boot auto configuration
	 */
	protected BearerTokenAuthenticationFilter createBearerTokenAuthenticationFilter() {
		BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(getAuthenticationManager());
		filter.setBearerTokenResolver(new DefaultBearerTokenResolver());
		filter.setAuthenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
		
		return filter;
	}
	
	/*
	 * Return the value of the Bearer token as it would appear in the header
	 */
	public String getJwtHeader() throws Exception {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.roll(Calendar.DATE, true);
		
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
									.subject(SUBJECT)
									.claim("claim1", CLAIM1)
									.expirationTime(tomorrow.getTime())
									.build();
		JWK jwk = getJWK();

		
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
								.keyID(jwk.getKeyID())
								.build();
		Payload payload = new Payload(claimsSet.toJSONObject());
		
		JWSObject jwsObject = new JWSObject(header, payload);
		
		JWSSigner signer = new RSASSASigner((RSAPrivateKey)getPrivateKey());
		
		jwsObject.sign(signer);
		

		return jwsObject.serialize();
	}
	
	/*
	 * Create the AuthenticationManager
	 */
	private AuthenticationManager getAuthenticationManager() {
		JwtAuthenticationProvider provider = new JwtAuthenticationProvider(getDecoder());
		provider.setJwtAuthenticationConverter(getConverter());
		ProviderManager authenticationManager = new ProviderManager(Arrays.asList(provider));
		
		return authenticationManager;
	}
	
	/*
	 * Create the default converter from .oauth2ResourceServer() configurer)
	 */
	private JwtAuthenticationConverter getConverter() {
		return new JwtAuthenticationConverter();

	}
	
	/*
	 * Create the decoder that Spring Boot creates by default using application.properties (and configurer)
	 * but with a custom mock RestOperations that's used by RestOperationsResourceRetriever,
	 * which is in a RemoteJWKSet (JWKSource<SecurityContext>)
	 * which is in a JWSVerificationKeySelector<> (JWSKeySelector<SecurityContext>)
	 * which is in a DefaultJWTProcessor (ConfigurableJWTProcessor<SecurityContext>)
	 * which the NimbusJwtDecoder is initialized with
	 */
	private JwtDecoder getDecoder() {
		RestOperations mockOperations = mock(RestTemplate.class);
		doReturn(new ResponseEntity<String>(getJwkSet(), HttpStatus.ACCEPTED)).when(mockOperations).exchange(any(), eq(String.class));
		
		NimbusJwtDecoderJwkSupport decoder = new NimbusJwtDecoderJwkSupport(getPropertiesJwkSetUri());	
		decoder.setRestOperations(mockOperations);
		
		return decoder;

	}
	
	/*
	 * Return the jwk set returned by the authorization server
	 */
	public String getJwkSet() {

		JWK jwk = null;
		try {
			jwk = getJWK();
		} catch(Exception ex) {
			throw new RuntimeException("Error converting to JWK");
		}
		
		return "{\"keys\":[" + jwk.toPublicJWK().toString() + "]}";
	}
	
	private String getPropertiesJwkSetUri() {
		return "https://ah-events-authorization.com/.well-known/jwks.json";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private PrivateKey getPrivateKey() throws Exception {
		File file = ResourceUtils.getFile("classpath:crypto/private_key.der");
		
		byte[] keyBytes = Files.readAllBytes(file.toPath());

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		
		return kf.generatePrivate(spec);
		
	}
	
	private PublicKey getPublicKey() throws Exception {
		File file = ResourceUtils.getFile("classpath:crypto/public_key.der");
		
		byte[] keyBytes = Files.readAllBytes(file.toPath());

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		
		return kf.generatePublic(spec);
		
	}
	
	JWK getJWK() throws Exception {
		PrivateKey priv = getPrivateKey();
		PublicKey pub = getPublicKey();
		
		JWK jwk = new RSAKey.Builder((RSAPublicKey)pub)
			    .privateKey((RSAPrivateKey)priv)
			    .keyUse(KeyUse.SIGNATURE)
			    .keyID(UUID.randomUUID().toString())
			    .build();
		
		return jwk;
	}
	
	
	
}
