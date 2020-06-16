package com.splitoil.travel.travel.domain.event;

import com.splitoil.shared.event.DomainEvent;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value
public class CarInitialStateSet implements DomainEvent {
    @NonNull UUID eventId = UUID.randomUUID();
    @NonNull UUID aggregateId;
    @NonNull Instant createdAt = Instant.now();
    @NonNull UUID carId;
    @NonNull BigDecimal currentFuelLevel;
    int odometer;
}
