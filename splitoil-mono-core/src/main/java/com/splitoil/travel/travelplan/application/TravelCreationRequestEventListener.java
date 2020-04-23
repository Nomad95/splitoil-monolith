package com.splitoil.travel.travelplan.application;

import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TravelCreationRequestEventListener {

    @Async
    @EventListener
    public void handle(final @NonNull TravelCreationRequested travelCreationRequestedEvent) {
        //create Travel
        System.out.println(travelCreationRequestedEvent);
    }
}
