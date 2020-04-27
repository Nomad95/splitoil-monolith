package com.splitoil.travel.lobby.application;

import com.splitoil.travel.travelflow.domain.event.TravelCreated;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TravelCreatedEventListener {

    private final LobbyFacade lobbyFacade;

    @Async
    @EventListener
    public void handle(final @NonNull TravelCreated travelCreatedEvent) {
        lobbyFacade.assignTravelToLobby(travelCreatedEvent.getAggregateId(), travelCreatedEvent.getLobbyId());
    }
}
