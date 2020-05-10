package com.splitoil.car.domain;

import com.splitoil.shared.UserCurrencyProvider;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.event.publisher.NoopEventPublisher;
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CarConfiguration {

    CarFacade carFacade() {
        return new CarFacade(new CarCreator(LobbyConfiguration.StubCurrencyProvider.getInstance()), new InMemoryCarsRepository(),
            new InMemoryCarCostRepository(), new InMemoryCarRefuelRepository(), new NoopEventPublisher());
    }

    @Bean
    CarFacade carFacade(final CarsRepository carsRepository, final CarCostRepository carCostRepository, final CarRefuelRepository carRefuelRepository, final
    EventPublisher eventPublisher, final UserCurrencyProvider userCurrencyProvider) {
        final CarCreator carCreator = new CarCreator(userCurrencyProvider);
        return new CarFacade(carCreator, carsRepository, carCostRepository, carRefuelRepository, eventPublisher);
    }
}
