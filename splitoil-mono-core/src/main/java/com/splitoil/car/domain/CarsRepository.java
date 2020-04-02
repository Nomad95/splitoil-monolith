package com.splitoil.car.domain;

import com.splitoil.car.dto.CarView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface CarsRepository extends CrudRepository<Car, Long> {

    List<Car> findAllByOwner(Driver owner);

    @Query("SELECT new com.splitoil.car.dto.CarView(c.aggregateId, c.brand, c.name, c.owner.driverId, c.mileage) FROM Car c WHERE c.owner = :owner")
    List<CarView> findAllByOwnerView(@Param("owner") Driver owner);

    Optional<Car> findByAggregateId(final UUID aggregateId);

    default Car getOneByAggregateId(final UUID aggregateId) {
        return findByAggregateId(aggregateId).orElseThrow(() -> new EntityNotFoundException(String.format("Car with id %s was not found", aggregateId)));
    }
}
