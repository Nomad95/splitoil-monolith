package com.splitoil.travel.flowcontrol.domain.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlowControlRepository extends CrudRepository<FlowControl, Long> {

    Optional<FlowControl> findByAggregateId(final UUID aggregateId);

    default FlowControl getOneByAggregateId(final UUID aggregateId) {
        return findByAggregateId(aggregateId).orElseThrow(() -> new EntityNotFoundException(String.format("Flow Control with id %s was not found", aggregateId)));
    }
}
