package com.splitoil.shared.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID getEventId();

    UUID getAggregateId();

    Instant getCreatedAt();

}