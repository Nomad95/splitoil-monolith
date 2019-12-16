package com.splitoil.gasstation.domain;

import java.util.List;

interface ObservedGasStationsRepository {

    void addGasStationToObservables(ObservedGasStation observedGasStation);

    List<ObservedGasStation> findByDriver(Driver driver);
}
