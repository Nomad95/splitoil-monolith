package com.splitoil.gasstation.domain;

class GasStationConfiguration {

    GasStationsFacade gasStationsFacade() {
        return new GasStationsFacade(new InMemoryObservedGasStationsRepository(), new InMemoryGasStationRepository(), new GasStationCreator());
    }
}
