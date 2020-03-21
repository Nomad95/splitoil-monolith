package com.splitoil.travel.lobby.infrastructure;

import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.event.publisher.NoopEventPublisher;
import com.splitoil.travel.lobby.application.LobbyService;
import com.splitoil.travel.lobby.domain.model.LobbyCreator;
import com.splitoil.travel.lobby.domain.model.LobbyRepository;
import com.splitoil.travel.lobby.infrastructure.database.InMemoryLobbyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LobbyConfiguration {

    public LobbyService lobbyService() {
        return new LobbyService(new LobbyCreator(), new InMemoryLobbyRepository(), new NoopEventPublisher());
    }

    @Bean
    public LobbyService lobbyService(final LobbyRepository lobbyRepository, final EventPublisher eventPublisher) {
        final LobbyCreator lobbyCreator = new LobbyCreator();
        return new LobbyService(lobbyCreator, lobbyRepository, eventPublisher);
    }

}
