package com.splitoil.travel.travel.domain.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TravelRepository extends CrudRepository<Travel, Long> {

    Optional<Travel> findByAggregateId(final UUID aggregateId);

    default Travel getByAggregateId(final UUID aggregateId) {
        return findByAggregateId(aggregateId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Travel %s does not exist", aggregateId)));
    }
}
