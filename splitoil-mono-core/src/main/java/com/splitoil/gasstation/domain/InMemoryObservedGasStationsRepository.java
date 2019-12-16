package com.splitoil.gasstation.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InMemoryObservedGasStationsRepository implements ObservedGasStationsRepository {

    private final Map<Long, ObservedGasStation> observedGasStationMap = new HashMap<>();

    @Override
    public void addGasStationToObservables(final ObservedGasStation observedGasStation) {
        observedGasStationMap.put(observedGasStation.getId(), observedGasStation);
    }

    @Override
    public List<ObservedGasStation> findByDriver(final Driver driver) {
        return observedGasStationMap.values().stream()
            .filter(station -> station.getObserver().equals(driver))
            .collect(Collectors.toList());
    }
}
