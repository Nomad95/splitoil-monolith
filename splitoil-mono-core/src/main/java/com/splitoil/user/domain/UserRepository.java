package com.splitoil.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByLogin(final String username);

    default ApplicationUser getOneByLogin(final String username) {
        return findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(String.format("user %s was not found", username)));
    }

    Optional<ApplicationUser> findByAggregateId(final UUID aggregateId);

    default ApplicationUser getOneById(final UUID id) {
        return findByAggregateId(id).orElseThrow(() -> new UsernameNotFoundException(String.format("user with id %s was not found", id)));
    }
}
