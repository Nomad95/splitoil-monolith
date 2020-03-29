package com.splitoil.user.shared;

import com.splitoil.user.infrastructure.SecurityPrincipal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    public static String getCurrentUserLogin() {
        if (nonNull(SecurityContextHolder.getContext().getAuthentication()) &&
            nonNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof SecurityPrincipal) {
                return ((SecurityPrincipal) principal).getUsername();
            } else return SecurityContextHolder.getContext().getAuthentication().getName();
        }

        throw new IllegalStateException("Could not get current user login");
    }

    public static boolean isUserLoggedIn() {
        return nonNull(SecurityContextHolder.getContext().getAuthentication())
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
