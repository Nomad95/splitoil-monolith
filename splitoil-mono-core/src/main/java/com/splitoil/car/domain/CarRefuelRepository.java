package com.splitoil.car.domain;

import com.splitoil.car.dto.RefuelCarOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CarRefuelRepository extends CrudRepository<CarRefuel, Long> {

    @Query("SELECT new com.splitoil.car.dto.RefuelCarOutputDto(cr.id, cr.costDate, cr.gasStation.name, cr.value, cr.fuelAmountInLitres, CAST(cr.petrolType as text)) "
               + "FROM CarRefuel cr WHERE cr.carId = :carId ORDER BY cr.costDate DESC")
    Page<RefuelCarOutputDto> getRefuels(@Param("carId") Long carId, Pageable page);

    @Query("SELECT SUM(cr.value) FROM CarRefuel cr WHERE cr.carId = :carId")
    BigDecimal getTotalRefuelCostForCar(@Param("carId") Long carId);

    @Query("SELECT SUM(cr.fuelAmountInLitres) FROM CarRefuel cr WHERE cr.carId = :carId")
    BigDecimal getTotalRefuelAmountInLitres(@Param("carId") Long carId);
}
