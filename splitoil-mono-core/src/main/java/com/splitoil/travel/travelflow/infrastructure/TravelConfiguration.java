package com.splitoil.travel.travelflow.infrastructure;

import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.lobby.application.LobbyQuery;
import com.splitoil.travel.travelflow.application.TravelFlowFacade;
import com.splitoil.travel.travelflow.domain.model.TravelCreator;
import com.splitoil.travel.travelflow.domain.model.TravelRepository;
import com.splitoil.travel.travelflow.domain.model.TravelService;
import com.splitoil.travel.travelflow.infrastructure.database.InMemoryTravelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelConfiguration {

    public TravelFlowFacade travelFlowFacade(final EventPublisher eventPublisher, final LobbyQuery lobbyQuery) {
        return new TravelFlowFacade(new TravelCreator(), new TravelService(), new InMemoryTravelRepository(), eventPublisher, lobbyQuery);
    }

    @Bean
    public TravelFlowFacade travelFlowFacade(final TravelRepository travelRepository, final EventPublisher eventPublisher, final LobbyQuery lobbyQuery) {
        return new TravelFlowFacade(new TravelCreator(), new TravelService(), travelRepository, eventPublisher, lobbyQuery);
    }


}
