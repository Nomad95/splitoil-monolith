package com.splitoil.shared.event.publisher;

import com.splitoil.shared.event.DomainEvent;
import com.splitoil.shared.event.EventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JustForwardEventPublisher implements EventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(final DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
