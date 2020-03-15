package com.splitoil.car.domain.events;

import com.splitoil.shared.event.DomainEvent;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class CarAddedToCollection implements DomainEvent {
    @NonNull final UUID eventId = UUID.randomUUID();
    @NonNull final UUID aggregateId;
    @NonNull final Instant createdAt = Instant.now();
    @NonNull final Long driverId;
}
