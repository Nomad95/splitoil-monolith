package com.splitoil.travel.lobby.application;

import com.splitoil.travel.lobby.application.dto.ParticipantIdView;
import com.splitoil.travel.lobby.domain.model.Lobby;
import com.splitoil.travel.lobby.domain.model.LobbyRepository;
import com.splitoil.travel.lobby.web.dto.CarDto;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
public class LobbyQuery {

    private final LobbyRepository lobbyRepository;

    public boolean carsExistInLobby(final List<UUID> carIds, final UUID lobbyId) {
        //TODO: how to get only json obj with JPQL?
        final Lobby lobby = lobbyRepository.getByAggregateId(lobbyId);
        final List<UUID> lobbyCarsIds = lobby.toDto().getCars().stream().map(CarDto::getId).collect(Collectors.toUnmodifiableList());

        for (final UUID carId : carIds) {
            if (!lobbyCarsIds.contains(carId))
                return false;
        }

        return true;
    }

    public boolean carExistInLobby(final UUID carId, final UUID lobbyId) {
        final Lobby lobby = lobbyRepository.getByAggregateId(lobbyId);

        return lobby.toDto().getCars().stream()
            .map(CarDto::getId)
            .collect(Collectors.toUnmodifiableList())
            .contains(carId);
    }

    public boolean participantExistsInLobby(final UUID reseatedParticipantId, final UUID lobbyId) {
        final List<ParticipantIdView> lobbyPassengersView = lobbyRepository.getLobbyPassengersView(lobbyId);
        return lobbyPassengersView.stream()
            .map(ParticipantIdView::getId)
            .anyMatch(id -> id.equals(reseatedParticipantId));
    }
}
