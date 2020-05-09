package com.splitoil.travel.lobby.domain.model;

import com.splitoil.travel.lobby.application.dto.ParticipantIdView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LobbyRepository extends CrudRepository<Lobby, Long> {

    Optional<Lobby> findByAggregateId(final UUID aggregateId);

    default Lobby getByAggregateId(final UUID aggregateId) {
        return findByAggregateId(aggregateId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Lobby %s does not exist", aggregateId)));
    }

    @Query("SELECT new com.splitoil.travel.lobby.application.dto.ParticipantIdView(tp.aggregateId) FROM TravelParticipant tp where tp.lobby.aggregateId = :lobbyId")
    List<ParticipantIdView> getLobbyPassengersView(final UUID lobbyId);
}
