package com.splitoil.shared.event.publisher;

import com.splitoil.shared.event.DomainEvent;
import com.splitoil.shared.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoopEventPublisher implements EventPublisher {

    @Override
    public void publish(final DomainEvent event) {
        log.info("event {} published", event.getEventId());
    }
}
