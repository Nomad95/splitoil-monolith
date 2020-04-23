package com.splitoil.travel.lobby.domain.event;

import com.splitoil.shared.event.DomainEvent;
import com.splitoil.travel.lobby.web.dto.ForTravelCreationLobbyDto;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
public class TravelCreationRequested implements DomainEvent {
    @NonNull UUID eventId = UUID.randomUUID();
    @NonNull UUID aggregateId;
    @NonNull Instant createdAt = Instant.now();
    @NonNull ForTravelCreationLobbyDto forTravelCreationLobbyDto;
}
