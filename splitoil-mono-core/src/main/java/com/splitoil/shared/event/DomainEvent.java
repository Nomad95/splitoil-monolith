package com.splitoil.shared.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent { //TODO: zmienic to jakos z tym aggregateId

    UUID getEventId();

    UUID getAggregateId();

    Instant getCreatedAt();

}
