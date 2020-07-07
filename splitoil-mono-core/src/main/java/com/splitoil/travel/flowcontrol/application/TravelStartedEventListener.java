package com.splitoil.travel.flowcontrol.application;

import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.flowcontrol.application.command.CreateFlowControlCommand;
import com.splitoil.travel.lobby.application.LobbyQuery;
import com.splitoil.travel.travel.domain.event.TravelStarted;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TravelStartedEventListener {

    private final LobbyQuery lobbyQuery;

    private final EventPublisher eventPublisher;

    @Async
    @EventListener
    public void handle(final @NonNull TravelStarted travelStarted) {
        final List<UUID> carsIds = lobbyQuery.getLobbyCarsIdsByTravelId(travelStarted.getAggregateId());

        carsIds.forEach(carId -> eventPublisher.publish(new CreateFlowControlCommand(travelStarted.getAggregateId(), carId, travelStarted.getAggregateId())));
    }
}
