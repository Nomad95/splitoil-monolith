package com.splitoil.car.domain;

import com.splitoil.car.dto.CarView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarsRepository extends CrudRepository<Car, Long> {

    List<Car> findAllByOwner(Driver owner);

    @Query("SELECT new com.splitoil.car.dto.CarView(c.id, c.brand, c.name, c.owner.driverId, c.mileage) FROM Car c WHERE c.owner = :owner")
    List<CarView> findAllByOwnerView(@Param("owner") Driver owner);

}
