package com.qah.kiosk.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithJwtAuthenticationTokenFactory.class)
public @interface WithMockJwtAuthenticationToken {

	String username() default "test";

    String[] authorities() default "ROLE_USER";
        
}
