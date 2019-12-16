package com.splitoil.gasstation.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryGasStationRepository implements GasStationRepository {

    private final Map<GasStationId, GasStation> gasStationMap = new HashMap<>();

    @Override
    public Optional<GasStation> findOne(final GasStationId gasStationId) {
        return Optional.ofNullable(gasStationMap.get(gasStationId));
    }

    @Override
    public GasStation save(final GasStation gasStation) {
        gasStationMap.put(gasStation.getGasStation(), gasStation);
        return gasStation;
    }
}
