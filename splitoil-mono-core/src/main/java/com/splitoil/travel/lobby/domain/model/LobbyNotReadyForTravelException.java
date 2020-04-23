package com.splitoil.travel.lobby.domain.model;

import lombok.Getter;

import java.util.UUID;

public class
LobbyNotReadyForTravelException extends RuntimeException {

    @Getter
    private final UUID lobbyId;

    public LobbyNotReadyForTravelException(final UUID lobbyId) {
        this.lobbyId = lobbyId;
    }
}
