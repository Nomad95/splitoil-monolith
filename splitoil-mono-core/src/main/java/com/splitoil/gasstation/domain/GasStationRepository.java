package com.splitoil.gasstation.domain;

import java.util.Optional;

interface GasStationRepository {

    Optional<GasStation> findOne(final GasStationId gasStationId);

    GasStation save(final GasStation gasStation);
}
