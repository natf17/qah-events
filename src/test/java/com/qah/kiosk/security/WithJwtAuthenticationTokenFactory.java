package com.qah.kiosk.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;

public class WithJwtAuthenticationTokenFactory implements WithSecurityContextFactory<WithMockJwtAuthenticationToken> {

	@Override
	public SecurityContext createSecurityContext(WithMockJwtAuthenticationToken annotation) {
		String username = annotation.username();
		String[] authorities = annotation.authorities();
		List<String> authoritiesList = new ArrayList<>();
		Collections.addAll(authoritiesList, authorities);
				
		Instant issuedAt = Instant.now();
		Instant exp = issuedAt.plusSeconds(100);
		
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
				.keyID("sample-kid")
				.build();
				
		
		JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
		claimsSetBuilder.subject(username);
		
		Converter<Map<String, Object>, Map<String, Object>> claimSetConverter =
				MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
		Map<String, Object> claims = claimSetConverter.convert(claimsSetBuilder.build().getClaims());
		
		
		
		Jwt jwt = new Jwt("tokenValue", issuedAt, exp, new LinkedHashMap<String,Object>(header.toJSONObject()), claims);
		
		Collection<GrantedAuthority> grantedAuthorities = authoritiesList.stream()
																			.map(i -> new SimpleGrantedAuthority(i))
																			.collect(Collectors.toList());
		
		Authentication auth = new JwtAuthenticationToken(jwt, grantedAuthorities);
		
		
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		
		context.setAuthentication(auth);
		 
		return context;
	}

}
