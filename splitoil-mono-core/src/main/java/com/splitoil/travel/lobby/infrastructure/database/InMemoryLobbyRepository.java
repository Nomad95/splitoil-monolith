package com.splitoil.travel.lobby.infrastructure.database;

import com.splitoil.infrastructure.json.JacksonAdapter;
import com.splitoil.shared.CrudInMemoryRepository;
import com.splitoil.travel.lobby.application.dto.ParticipantIdView;
import com.splitoil.travel.lobby.domain.model.Lobby;
import com.splitoil.travel.lobby.domain.model.LobbyRepository;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InMemoryLobbyRepository extends CrudInMemoryRepository<Lobby> implements LobbyRepository {

    @Override public List<ParticipantIdView> getLobbyPassengersView(final UUID lobbyId) {
        final Optional<Lobby> any = map.values().stream().filter(lobby -> lobby.getAggregateId().equals(lobbyId)).findAny();
        if (any.isEmpty()) {
            return List.of();
        }

        return any.get().toDto().getParticipants().stream()
            .map(LobbyParticipantDto::getUserId)
            .map(ParticipantIdView::new)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override public String getCarsJson(final UUID lobbyId) {
        return findByAggregateId(lobbyId)
            .map(Lobby::getCars)
            .map(e -> JacksonAdapter.getInstance().toJson(e)).orElse(null);
    }

    @Override
    public String getCarsJsonByTravelId(final UUID travelId) {
        return StreamSupport.stream(findAll().spliterator(), false)
            .filter(l -> travelId.equals(l.getTravelId().getTravelId()))
            .map(Lobby::getCars)
            .map(e -> JacksonAdapter.getInstance().toJson(e))
            .findFirst()
            .orElse(null);
    }
}
