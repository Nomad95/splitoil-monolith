package com.splitoil.travel.lobby.domain.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LobbyRepository extends CrudRepository<Lobby, Long> {

    Optional<Lobby> findByAggregateId(final UUID aggregateId);

    default Lobby getByAggregateId(final UUID aggregateId) {
        return findByAggregateId(aggregateId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Lobby %s does not exist", aggregateId)));
    }
}
