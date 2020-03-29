package com.splitoil.infrastructure.security;

import com.splitoil.user.infrastructure.ApplicationUserWebAuthenticationDetails;
import com.splitoil.user.infrastructure.SecurityPrincipal;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockSecurityContext> {

    @Override
    public SecurityContext createSecurityContext(final WithMockSecurityContext withMockSecurityContext) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        final List<SimpleGrantedAuthority> authorities = Stream.of(withMockSecurityContext.roles()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        SecurityPrincipal principal = SecurityPrincipal.builder()
            .id(1L)
            .username(withMockSecurityContext.login())
            .password(withMockSecurityContext.password())
            .authorities(authorities)
            .enabled(true)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .build();

        ApplicationUserWebAuthenticationDetails details = new ApplicationUserWebAuthenticationDetails(new MockHttpServletRequest(), withMockSecurityContext.currentUserName());
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, withMockSecurityContext.password(), authorities);
        auth.setDetails(details);

        context.setAuthentication(auth);
        return context;
    }
}
