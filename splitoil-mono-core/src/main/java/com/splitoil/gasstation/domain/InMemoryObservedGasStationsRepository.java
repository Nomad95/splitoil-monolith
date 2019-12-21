package com.splitoil.gasstation.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class InMemoryObservedGasStationsRepository implements ObservedGasStationsRepository {

    private final Map<Long, ObservedGasStation> observedGasStationMap = new HashMap<>();

    @Override public <S extends ObservedGasStation> S save(final S entity) {
        observedGasStationMap.put(entity.getId(), entity);
        return entity;
    }

    @Override public <S extends ObservedGasStation> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override public Optional<ObservedGasStation> findById(final Long aLong) {
        return Optional.empty();
    }

    @Override public boolean existsById(final Long aLong) {
        return false;
    }

    @Override public Iterable<ObservedGasStation> findAll() {
        return null;
    }

    @Override public Iterable<ObservedGasStation> findAllById(final Iterable<Long> longs) {
        return null;
    }

    @Override public long count() {
        return 0;
    }

    @Override public void deleteById(final Long aLong) {

    }

    @Override public void delete(final ObservedGasStation entity) {

    }

    @Override public void deleteAll(final Iterable<? extends ObservedGasStation> entities) {

    }

    @Override public void deleteAll() {

    }

    @Override public List<ObservedGasStation> findAllByObserver(final Driver observer) {
        return observedGasStationMap.values().stream()
            .filter(station -> station.getObserver().equals(observer))
            .collect(Collectors.toList());
    }
}
