package com.splitoil.car.domain;

import com.splitoil.car.dto.CarCostDto;
import com.splitoil.shared.CrudInMemoryRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.UUID;

class InMemoryCarCostRepository extends CrudInMemoryRepository<CarCost> implements CarCostRepository {

    @Override
    public BigDecimal getSumCostForCarId(final UUID carId) {
        return map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(CarCost::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override public CarCostDto getCarCostDto(final UUID id) {
        return map.values().stream()
            .filter(v -> v.getAggregateId().equals(id))
            .map(cc -> new CarCostDto(cc.getName(), cc.getCostDate(), cc.getValue(), cc.getCarId(), cc.getCurrency().name()))
            .findFirst().orElseThrow(() -> new EntityNotFoundException("O no no!"));
    }
}
