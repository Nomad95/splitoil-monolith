package com.splitoil.gasstation.domain;

import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.event.publisher.NoopEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GasStationConfiguration {

    GasStationsFacade gasStationsFacade() {
        return new GasStationsFacade(new InMemoryObservedGasStationsRepository(), new InMemoryGasStationRepository(), new GasStationCreator(),
            new InMemoryPetrolPriceRepository(), new NoopEventPublisher());
    }

    @Bean
    GasStationsFacade gasStationsFacade(final ObservedGasStationsRepository observedGasStationsRepository, final GasStationRepository gasStationRepository,
        final PetrolPriceRepository petrolPriceRepository, final EventPublisher eventPublisher) {
        final GasStationCreator gasStationCreator = new GasStationCreator();
        return new GasStationsFacade(observedGasStationsRepository, gasStationRepository, gasStationCreator, petrolPriceRepository, eventPublisher);
    }
}
