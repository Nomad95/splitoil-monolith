package com.splitoil.travel.travelflow.domain.event;

import com.splitoil.shared.event.DomainEvent;
import com.splitoil.travel.travelflow.web.dto.GeoPointDto;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class TravelStopPlaceAdded implements DomainEvent {
    @NonNull UUID eventId = UUID.randomUUID();
    @NonNull UUID aggregateId;
    @NonNull Instant createdAt = Instant.now();
    @NonNull GeoPointDto location;
}
