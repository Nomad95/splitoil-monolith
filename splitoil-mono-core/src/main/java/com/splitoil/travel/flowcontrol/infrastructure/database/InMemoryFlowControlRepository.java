package com.splitoil.travel.flowcontrol.infrastructure.database;

import com.splitoil.shared.CrudInMemoryRepository;
import com.splitoil.travel.flowcontrol.domain.model.FlowControl;
import com.splitoil.travel.flowcontrol.domain.model.FlowControlRepository;

public class InMemoryFlowControlRepository extends CrudInMemoryRepository<FlowControl> implements FlowControlRepository {

}
