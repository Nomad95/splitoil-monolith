package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.annotation.DomainService;
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@DomainService
@AllArgsConstructor
public class LobbyService {

    private final LobbyCreator lobbyCreator;

    public TravelCreationRequested startDefiningTravelPlan(final @NonNull Lobby lobby) {
        if (!lobby.canStartDefiningTravel()) {
            throw new LobbyNotReadyForTravelException(lobby.getAggregateId());
        }

        return lobbyCreator.createRequestForTravelCreation(lobby);
    }

}
