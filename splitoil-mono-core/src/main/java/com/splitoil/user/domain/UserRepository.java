package com.splitoil.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByLogin(final String username);

    default ApplicationUser getOneByLogin(final String username) {
        return findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(String.format("user %s was not found", username)));
    }
}
