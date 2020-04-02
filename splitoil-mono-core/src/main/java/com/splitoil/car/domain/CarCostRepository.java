package com.splitoil.car.domain;

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

}
