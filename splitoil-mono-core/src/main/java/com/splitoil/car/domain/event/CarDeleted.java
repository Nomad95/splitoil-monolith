package com.splitoil.car.domain.event;

import com.splitoil.shared.event.DomainEvent;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class CarDeleted implements DomainEvent {
    @NonNull UUID eventId = UUID.randomUUID();
    @NonNull UUID aggregateId;
    @NonNull Instant createdAt = Instant.now();
}
