package com.splitoil.car.domain;

import com.splitoil.car.dto.RefuelCarOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
interface CarRefuelRepository extends CrudRepository<CarRefuel, Long> {

    @Query("SELECT new com.splitoil.car.dto.RefuelCarOutputDto(cr.aggregateId, cr.costDate, cr.gasStation.name, cr.value, cr.fuelAmountInLitres, CAST(cr.petrolType as text)) "
               + "FROM CarRefuel cr WHERE cr.carId = :carId ORDER BY cr.costDate DESC")
    Page<RefuelCarOutputDto> getRefuels(@Param("carId") UUID carId, Pageable page);

    @Query("SELECT SUM(cr.value) FROM CarRefuel cr WHERE cr.carId = :carId")
    BigDecimal getTotalRefuelCostForCar(@Param("carId") UUID carId);

    @Query("SELECT SUM(cr.fuelAmountInLitres) FROM CarRefuel cr WHERE cr.carId = :carId")
    BigDecimal getTotalRefuelAmountInLitres(@Param("carId") UUID carId);
}
