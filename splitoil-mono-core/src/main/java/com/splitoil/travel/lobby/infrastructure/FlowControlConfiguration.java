package com.splitoil.travel.lobby.infrastructure;

import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.flowcontrol.application.FlowControlFacade;
import com.splitoil.travel.flowcontrol.application.FlowControlQuery;
import com.splitoil.travel.flowcontrol.domain.model.FlowControlCreator;
import com.splitoil.travel.flowcontrol.domain.model.FlowControlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowControlConfiguration {

    public FlowControlFacade flowControl(final EventPublisher eventPublisher, final FlowControlRepository flowControlRepository) {
        return new FlowControlFacade(eventPublisher, flowControlRepository, new FlowControlCreator());
    }

    @Bean
    public FlowControlFacade flowControlFacade(final EventPublisher eventPublisher, final FlowControlRepository flowControlRepository) {
        return new FlowControlFacade(eventPublisher, flowControlRepository, new FlowControlCreator());
    }

    @Bean
    public FlowControlQuery flowControlQuery(final FlowControlRepository flowControlRepository) {
        return new FlowControlQuery(flowControlRepository);
    }


}
