package com.splitoil.car.domain;

import com.splitoil.car.dto.CarView;
import com.splitoil.shared.CrudInMemoryRepository;

import java.util.List;
import java.util.stream.Collectors;

class InMemoryCarsRepository extends CrudInMemoryRepository<Car> implements CarsRepository {

    @Override
    public List<Car> findAllByOwner(final Driver owner) {
        return map.values().stream().filter(c -> c.getOwner().equals(owner)).collect(Collectors.toList());
    }

    @Override
    public List<CarView> findAllByOwnerView(final Driver owner) {
        return map.entrySet().stream().filter(c -> c.getValue().getOwner().equals(owner))
            .map(c ->
                CarView.builder().brand(c.getValue().getBrand()).name(c.getValue().getName()).driverId(c.getValue().getOwner().getDriverId()).id(c.getValue().getAggregateId()).build()
            )
            .collect(Collectors.toList());
    }

}
