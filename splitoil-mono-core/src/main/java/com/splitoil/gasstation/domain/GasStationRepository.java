package com.splitoil.gasstation.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface GasStationRepository extends CrudRepository<GasStation, Long> {

    Optional<GasStation> findOptionalByGasStation(final GasStationId gasStationId);
}
