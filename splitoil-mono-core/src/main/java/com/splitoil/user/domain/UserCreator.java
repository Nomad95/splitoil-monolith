package com.splitoil.user.domain;

import com.splitoil.shared.model.Currency;
import com.splitoil.user.web.dto.RegisterUserCommand;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@AllArgsConstructor
public class UserCreator {

    private final PasswordEncoder passwordEncoder;

    public ApplicationUser createApplicationUser(final RegisterUserCommand registerUserCommand) {
        return ApplicationUser.builder()
            .login(registerUserCommand.getLogin())
            .email(registerUserCommand.getEmail())
            .defaultCurrency(Currency.valueOf(registerUserCommand.getDefaultCurrency()))
            .password(passwordEncoder.encode(registerUserCommand.getPassword()))
            .roles( new UserRoles(Set.of("ROLE_USER")))
            .build();
    }
}
