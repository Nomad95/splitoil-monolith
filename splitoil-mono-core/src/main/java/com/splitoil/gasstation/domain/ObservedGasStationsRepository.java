package com.splitoil.gasstation.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ObservedGasStationsRepository extends CrudRepository<ObservedGasStation, Long> {

    List<ObservedGasStation> findAllByObserver(Driver observer);
}
