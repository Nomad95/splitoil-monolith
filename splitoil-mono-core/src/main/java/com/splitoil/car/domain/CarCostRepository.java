package com.splitoil.car.domain;

import com.splitoil.car.dto.CarCostDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
interface CarCostRepository extends CrudRepository<CarCost, Long> {

    @Query("SELECT SUM(cc.value) FROM CarCost cc WHERE cc.carId = :carId")
    BigDecimal getSumCostForCarId(@Param("carId") UUID carId);

    @Query("SELECT new com.splitoil.car.dto.CarCostDto(cc.name, cc.costDate, cc.value, cc.carId, CAST(cc.currency as text)) FROM CarCost cc where cc.aggregateId = :carCostId")
    CarCostDto getCarCostDto(final UUID carCostId);
}
