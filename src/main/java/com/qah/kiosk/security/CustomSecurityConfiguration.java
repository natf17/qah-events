package com.qah.kiosk.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 * Spring Boot autoconfiguration: OAuth2ResourceServerAutoConfiguration
 * - instantiates a OAuth2ResourceServerProperties from application.properties
 * - imports OAuth2ResourceServerJwtConfiguration
 * - imports OAuth2ResourceServerWebSecurityConfiguration (simply enables resource server
 *   support on HttpSecurity (it's not enabled because we provide our own WebSecurityConfigurerAdapter)
 *   
 *   In OAuth2ResourceServerJwtConfiguration, if spring.security.oauth2.resourceserver.jwt.jwk-set-uri found: 
 *   
 *   (... not yet as of boot 2.1)
 *   
 *   NimbusJwtDecoder.withJwkSetUri(this.properties.getJwkSetUri())
 *				.jwsAlgorithm(SignatureAlgorithm.from(this.properties.getJwsAlgorithm()))
 *				.build();
 * 
 *  ** By default, in OAuth2ResourceServerProperties, the algorithm is RS256
 *  
 *  
 *  
 *  OAuth2ResourceServerConfigurer
 *  init:
		* gets ExceptionHandlingConfigurer from http (that WebSecurityConfigurer adds by default)
 *  		- registers accessDeniedHandler for OAuth2ResourceServerConfigurer requestMatcher:
 *  			exceptionHandlingConfigurer.defaultAccessDeniedHandlerFor(this.accessDeniedHandler,
 *  				this.requestMatcher);
 *  
 *  				... BearerTokenAccessDeniedHandler and BearerTokenRequestMatcher
 *  
 *  		- registers authenticationEntryPoint for OAuth2ResourceServerConfigurer requestMatcher:
 *  			exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(this.authenticationEntryPoint,
 *  				this.requestMatcher);
 *  
 *  				... BearerTokenAuthenticationEntryPoint and BearerTokenRequestMatcher
 *  
 *  		- use HttpSecurity's CsrfConfigurer to disable csrf for this.requestMatcher
 *  
 *  
 *  configure:
 *  	1. Look for BearerTokenResolver: configurer : bean : DefaultBearerTokenResolver
 *  		None provided by Spring Boot
 *  	2. Encapsulate HttpSecurity's AuthenticationManager in an AuthenticationManagerResolver (not yet as of 5.1)
 *  	3. new BearerTokenAuthenticationFilter() and add to HttpSecurity
 *  		set:
 *  		- bearerTokenResolver
 *  		- authenticationEntryPoint
 *  	4. Since .oauth2ResourceServer().jwt() -> then JwtConfigurer is found
 *  	5. Get JwtDecoder from JwtConfigurer -> returns bean found in A.C
 *  		- Spring Boot provides one in OAuth2ResourceServerJwtConfiguration
 *  	6. Get jwtAuthenticationConverter (Converter<Jwt, ? extends AbstractAuthenticationToken>)
 *  	   from JwtConfigurer... default is returned: JwtAuthenticationConverter
 *  	7. new JwtAuthenticationProvider(decoder)
 *  	8. Set jwtAuthenticationConverter on jwtAuthenticationProvider
 *  	9. Add the jwtAuthenticationProvider to HttpSecurity 
 *  
 *  See NimbusJwtDecoder for more info...
 *  
 *  When the NimbusJwtDecoder is built by Spring Boot (OAuth2ResourceServerJwtConfiguration),
 *  a JWTProcessor<SecurityContext> is created and passed in to the decoder as a constructor arg.
 *  It is DefaultJWTProcessor, which contains:
 *  
 *  1. JWSVerificationKeySelector<>(this.jwsAlgorithm, jwkSource)
 *  	The jwkSource is a RemoteJWKSet(impl of JwsKeySelector<SecurityContext>) that contains the authorization
 *  	server url to retrieve its public keys (jwk) and an object that contains restOperations. It encapsulates
 *  	all we need to request the keys from the server.
 *  
 *  	The jwsAlgorithm is that which was set in the application.properties (default "RS256"),
 *  	if not the one the JwkSetUriJwtDecoderBuilder has by default (JWSAlgorithm.RS256).
 *  2. Claims verification "disabled"
 *  
 *  
 *  
 *  ... during Authentication:
 *  
 *  JwtAuthenticationProvider receives the BearerTokenAuthenticationToken in authenticate(auth)
 *  1. this.jwtDecoder.decode(bearer.getToken());
 *  	a. JWTParser.parse(token)
 *  		- use JOSEObject.split(s) to split the token into an array of parts (Base64URL[])
 *  	  	  by looking for dots in the string (ex: aaa.bbb.ccc for JWS)
 *  		- parse the header to construct a JSONObject
 *  		- Header.parseAlgorithm(jsonObject) returns the algorithm from the parsed header, which looks for the
 *  	  	  "alg" property. If it has an "enc" key, a JWE algorithm is expected; else a JWS algorithm
 *  	  	(see JWSAlgorithm or JWEAlgorithm for possibilities) is returned
 *  		- No algorithm? Return PlainJWT.parse(s)
 *  		- A JWSAlgorithm "type" was found? Return SignedJWT.parse(s)
 *  		- A JWEAlgorithm "type" was found? Return EncryptedJWT.parse(s);
 *  
 *  	Ex: At this point, for example...
 *  	
 *  	A SignedJWT object is created with the three parts of the token. The constructor of the JWSObject
 *  	superclass provides more info:
 *  		1. header: see JWSHeader.parse(string)
 *  		2. payload: property set on superclass of SignedJWT: JOSEObject; the payload Base64URL is wrapped
 *  		   in Payload.
 *  		3. signableContent: set on JWSObject superclass; consists of the bytes of
 *  		   <header string> + "." + <payload string> (ex: bytes of "aaa.bbb")
 *  		4. signature set on JWSObject; consists of Base64URL third part
 *  		5. state set on JWSObject: State.SIGNED;
 *  		6. parsedParts set on JOSEObject: Base64URL[] parsedParts (all 3)
 *  
 *  	b. apparently the decoder doesn't support EncryptedJWT????....
 *  	c. if the jwt is a SignedJWT...
 *  	d. decoder.createJwt(token, jwt);
 *  		- jwtProcessor.process(parsedJwt, null): returns a JWTClaimsSet (the payload)
 *  			... see DefaultJWTProcessor.process(SignedJWT, SecurityContextontext)
 *  			... at this point, the jwt has been verified, and we've made sure it hasn't expired
 *  		- the jwt headers are converted to Map<String, Object> headers
 *  		- the claims/payload are converted to Map<String, Object> claims. See: MappedJwtClaimSetConverter
 *  		  the converter converts many of the claims represented as strings into corresponding Objects
 *  		- the issuedAt and expiresAt claims are retrieved
 *  		- new Jwt(token, issuedAt, expiresAt, headers, claims) -> the token is the raw String
 *  		this Jwt is returned
 * 
 *  	e. decoder.validateJwt(createdJwt);
 *  		- Spring Boot uses JwtValidators.createDefault()
 *  	The Jwt is returned
 *  
 *  ------Validation in DefaultJWTProcessor.process(SignedJWT, SecurityContextontext)------
 *  	- get a List<? extends Key> keyCandidates by calling 
 *  	  ...JWSVerificationKeySelector.selectJWSKeys(signedJWT.getHeader(), null):
 *  		- The selector was initialized with the RSA256 algorithm, so it looks at the jwt's header
 *  		  to see if it's there (it must be!)
 *  		- A JWKMatcher is created; a different one depending on whether the algorithm is in the RSA family
 *  		  or HMAC... it contains info about the jws algorithm to use and the jwt header
 *  	 	- the selector's source (RemoteJWKSet) get(new JWKSelector(jwkMatcher), null) is called.
 *  			-- Get a JWKSet by either getting it from cache, or querying the authorization server to 
 *  			   obtain it. Remember we need its public key! The JWKSet contains an array in its "keys" property
 *  			-- Use the JWKSelector that contains the jwkMatcher (info from the jwt header...) and:
 *  			   jwkSelector.select(jwkSet). This returns a List<JWK> matches
 *  			-- If not found, refresh the cache and try again.
 *  		- Now that we have a List<JWK> matches, convert each to a Key, making sure they are instances of
 *  		  either PublicKey or SecretKey, and adding them to a List<Key> (java.security.Key)
 *  		This List<Key> is returned.
 *  	- get the processor's JWSVerifierFactory. The default is DefaultJWSVerifierFactory.
 *  	- for each key, use the JWSVerifierFactory to attempt to verify the signature. If it fails, try the
 *  	  next key until all are exhausted. If none matched, an exception is thrown
 *  	- when verification is successful: verifyAndReturnClaims(signedJWT, context)
 *  	- NOTE: Spring boot DISABLES the verification that follows
 *  	- verifyAndReturnClaims(signedJWT, context) -> at this point, the signedJWT has been VERIFIED
 *  		- get JWTClaimsSet from SignedJWT -> see JWTClaimsSet.parse(...). It returns a JWTClaimsSet that contains
 *  		  reserved params ("exp","nbf", "iss", "aud", ...) and custom params from payload
 *  		- use DefaultJWTClaimsVerifier: verifier.verify(claimsSet, context) -- context is null here...
 *  			- makes sure the jwt HASN'T EXPIRED by getting expiration from claims/payload
 *  	returns JWTClaimsSet
 *  	
 *  
 *  How is the signature verified?? See the processor and the JWSVerifierFactory:
 *  1. The processor creates a JWSVerifier by calling DefaultJWSVerifierFactory.createJWSVerifier(signedJWT.getHeader(), it.next())
 *  	- The JWSHeader and the Key must match 
 *  		(if the header says it's a mac verifier, the Key should be of type SecretKey;
 *  		if the header says it's an RSA verifier, the Key should be of type RSAPublicKey)
 *  	- A verifier is created with the Key only(ex: MACVerifier(macKey), RSASSAVerifier(rsaPublicKey), ...)
 *  	This verifier is returned.
 *  2. The processor passes the Verifier to the SignedJWT: signedJWT.verify(verifier)
 *  	- makes sure the SignedJWT is either SIGNED or VERIFIED state (at this point it should just be signed)
 *  	- makes sure the verifier accepts this jwt's algorithm in header, as well as ALL header parameters
 * 		- calls verifier.verify(getHeader(), getSignableContent(), getSignature()); which returns a boolean
 * 		- if verification was successful, the SignedJWT state is set as VERIFIED
 * 		true is returned
 * 	
 * 		ex: RSASSAVerifier.verify(ReadOnlyJWSHeader header, byte[] signedContent, Base64URL signature)
 *  ---------------------------------------------------------------------------------------
 *  
 *  2. AbstractAuthenticationToken token = this.jwtAuthenticationConverter.convert(jwt);
 *  ... The JwtAuthenticationConverter is the default that's used
 *  	a. creates a Collection<GrantedAuthority> by delegating to JwtGrantedAuthoritiesConverter:
 *  		- looks for the "scope" or "scp" property in the payload. The first one it finds that
 *  		  has either a String or a Collection<String> value will be used. The value(s) are wrapped in a Collection
 *  		- for every value under "scope" -> a SimpleGrantedAuthority is created with the "SCOPE_" prefix
 *  		  ex: "scope" = "e1 e1 e3" -> SimpleGrantedAuthority("SCOPE_e1"), SimpleGrantedAuthority("SCOPE_e2"), ...
 *  	b. creates a new JwtAuthenticationToken(jwt, authorities)
 *  	Returns the JwtAuthenticationToken
 *  
 *  3. BearerTokenAuthenticationToken.getDetails is called and the result is set on the new JwtAuthenticationToken:
 *  	JwtAuthenticationToken.setDetails(...)
 *  	... the BearerTokenAuthenticationFilter had set the details. So see above
 *  
 *  The JwtAuthenticationProvider returns this JwtAuthenticationToken to the filter
 *  
 *  
 *  ===============================
 *  BearerTokenAuthenticationFilter
 *  ===============================
 *  
 *  1. Request comes in
 *  2. bearerTokenResolver.resolve(request) is called -> the default is DefaultBearerTokenResolver
 *  	... the resolver always tries to look for a token (in the header or as a parameter). If not found
 *  		it returns null.
 *  3. if an exception is thrown because multiple tokens were found, give control to authenticationEntryPoint
 *     (default: )
 *  4. if no token found, continue filter chain.
 *  5. create a new BearerTokenAuthenticationToken(token)
 *  6. ask authenticationDetailsSource to build details for the request:
 *  	(WebAuthenticationDetailsSource wraps the request in a WebAuthenticationDetails)
 *  7. set the details on the BearerTokenAuthenticationToken
 *  8. use the authenticationManager to authenticate: JwtAuthenticationProvider
 *  9. if there's an AuthenticationException... send to authenticationEntryPoint
 *  10. if successful, context.setAuthentication(JwtAuthenticationToken)
 *  11. Continue filter chain
 *  
 *  
 *  ===================================
 *  BearerTokenAuthenticationEntryPoint
 *  ===================================
 *  commence(...,...,...):
 *  1. Adds WWW-Authenticate header
 *  2. Sets UNAUTHORIZED status code
 *  
 *  ===================================
 *  BearerTokenAccessDeniedHandler
 *  ===================================
 *  handle(...,...,...):
 *  1. Adds WWW-Authenticate header
 *  2. Sets FORBIDDEN status code
 *  
 *  
 *  
 *  
 *  
 */
@EnableWebSecurity
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.csrf()
				.disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.GET).permitAll()
				.anyRequest().hasAnyRole("ADMIN", "USER")
				.and()
			.oauth2ResourceServer()
				.jwt();
	}
}
