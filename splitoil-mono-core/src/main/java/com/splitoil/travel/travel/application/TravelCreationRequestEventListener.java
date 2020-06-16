package com.splitoil.travel.travel.application;

import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TravelCreationRequestEventListener {

    private final TravelFlowFacade travelFlowFacade;

    @Async
    @EventListener
    public void handle(final @NonNull TravelCreationRequested travelCreationRequestedEvent) {
        travelFlowFacade.createNewTravel(travelCreationRequestedEvent);
    }
}
