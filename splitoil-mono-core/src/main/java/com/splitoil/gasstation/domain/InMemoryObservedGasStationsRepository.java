package com.splitoil.gasstation.domain;

import com.splitoil.shared.CrudInMemoryRepository;

import java.util.List;
import java.util.stream.Collectors;

class InMemoryObservedGasStationsRepository extends CrudInMemoryRepository<ObservedGasStation> implements ObservedGasStationsRepository {

    @Override public List<ObservedGasStation> findAllByObserver(final Driver observer) {
        return map.values().stream()
            .filter(station -> station.getObserver().equals(observer))
            .collect(Collectors.toList());
    }
}
