package com.splitoil.gasstation.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GasStationConfiguration {

    GasStationsFacade gasStationsFacade() {
        return new GasStationsFacade(new InMemoryObservedGasStationsRepository(), new InMemoryGasStationRepository(), new GasStationCreator());
    }

    @Bean
    GasStationsFacade gasStationsFacade(final ObservedGasStationsRepository observedGasStationsRepository, final GasStationRepository gasStationRepository) {
        final GasStationCreator gasStationCreator = new GasStationCreator();
        return new GasStationsFacade(observedGasStationsRepository, gasStationRepository, gasStationCreator);
    }
}
