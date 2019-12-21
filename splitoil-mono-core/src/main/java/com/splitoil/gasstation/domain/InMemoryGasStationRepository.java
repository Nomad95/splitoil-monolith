package com.splitoil.gasstation.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryGasStationRepository implements GasStationRepository {

    private final Map<GasStationId, GasStation> gasStationMap = new HashMap<>();

    @Override
    public Optional<GasStation> findOptionalByGasStation(final GasStationId gasStationId) {
        return Optional.ofNullable(gasStationMap.get(gasStationId));
    }

    @Override
    public GasStation save(final GasStation gasStation) {
        gasStationMap.put(gasStation.getGasStation(), gasStation);
        return gasStation;
    }

    @Override public <S extends GasStation> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override public Optional<GasStation> findById(final Long aLong) {
        return Optional.empty();
    }

    @Override public boolean existsById(final Long aLong) {
        return false;
    }

    @Override public Iterable<GasStation> findAll() {
        return null;
    }

    @Override public Iterable<GasStation> findAllById(final Iterable<Long> longs) {
        return null;
    }

    @Override public long count() {
        return 0;
    }

    @Override public void deleteById(final Long aLong) {

    }

    @Override public void delete(final GasStation entity) {

    }

    @Override public void deleteAll(final Iterable<? extends GasStation> entities) {

    }

    @Override public void deleteAll() {

    }
}
