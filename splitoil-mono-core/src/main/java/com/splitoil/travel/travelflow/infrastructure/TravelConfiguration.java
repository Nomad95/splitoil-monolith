package com.splitoil.travel.travelflow.infrastructure;

import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.travelflow.application.TravelFlowFacade;
import com.splitoil.travel.travelflow.domain.model.TravelCreator;
import com.splitoil.travel.travelflow.domain.model.TravelRepository;
import com.splitoil.travel.travelflow.infrastructure.database.InMemoryTravelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelConfiguration {

    public TravelFlowFacade travelFlowFacade(final EventPublisher eventPublisher) {
        return new TravelFlowFacade(new TravelCreator(), new InMemoryTravelRepository(), eventPublisher);
    }

    @Bean
    public TravelFlowFacade travelFlowFacade(final TravelRepository travelRepository, final EventPublisher eventPublisher) {
        return new TravelFlowFacade(new TravelCreator(), travelRepository, eventPublisher);
    }


}
