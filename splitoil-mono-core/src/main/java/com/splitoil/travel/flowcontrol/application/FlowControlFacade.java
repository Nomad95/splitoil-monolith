package com.splitoil.travel.flowcontrol.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ApplicationService
@AllArgsConstructor
public class FlowControlFacade {

    private final EventPublisher eventPublisher;
}
