package com.splitoil.gasstation.domain;

import com.splitoil.shared.CustomKeyCrudInMemoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryGasStationRepository extends CustomKeyCrudInMemoryRepository<GasStationId, GasStation> implements GasStationRepository {

    private final Map<GasStationId, GasStation> gasStationMap = new HashMap<>();

    @Override
    public Optional<GasStation> findOptionalByGasStation(final GasStationId gasStationId) {
        return Optional.ofNullable(gasStationMap.get(gasStationId));
    }

    @Override protected <S extends GasStation> void putElementToMap(final S gasStation) {
        gasStationMap.put(gasStation.getGasStation(), gasStation);
    }


}
