package com.splitoil.infrastructure.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockSecurityContext {

    String login() default "admin";

    String password() default "admin";

    String[] roles() default "ROLE_ADMIN";

    String currentUserName() default "Łukasz Borkowski";
}
