package com.splitoil.travel.travel.infrastructure.database;

import com.splitoil.shared.CrudInMemoryRepository;
import com.splitoil.travel.travel.domain.model.Travel;
import com.splitoil.travel.travel.domain.model.TravelRepository;

public class InMemoryTravelRepository extends CrudInMemoryRepository<Travel> implements TravelRepository {
}
