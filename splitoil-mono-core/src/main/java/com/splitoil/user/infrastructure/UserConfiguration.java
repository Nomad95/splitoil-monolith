package com.splitoil.user.infrastructure;

import com.splitoil.user.application.UserService;
import com.splitoil.user.domain.UserCreator;
import com.splitoil.user.domain.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfiguration {

    @Bean
    public UserService userService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, new UserCreator(passwordEncoder));
    }
}
