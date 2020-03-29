package com.splitoil.user.infrastructure;


import com.splitoil.user.domain.ApplicationUser;
import com.splitoil.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final ApplicationUser user = userRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException(format("User with username %s doesn't exist", username)));

        return SecurityPrincipal.builder()
            .id(user.getId())
            .username(user.getLogin())
            .password(user.getPassword())
            .authorities(user.getAuthorities())
            .enabled(true)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .build();
    }

}
