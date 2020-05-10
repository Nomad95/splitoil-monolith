package com.splitoil.travel.travelflow.infrastructure.database;

import com.splitoil.shared.CrudInMemoryRepository;
import com.splitoil.travel.travelflow.domain.model.Travel;
import com.splitoil.travel.travelflow.domain.model.TravelRepository;

public class InMemoryTravelRepository extends CrudInMemoryRepository<Travel> implements TravelRepository {
}
