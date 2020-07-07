package com.splitoil.travel.flowcontrol.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.travel.flowcontrol.domain.model.FlowControl;
import com.splitoil.travel.flowcontrol.domain.model.FlowControlRepository;
import com.splitoil.travel.flowcontrol.web.dto.FlowControlOutputDto;
import com.splitoil.travel.flowcontrol.web.dto.GpsPointDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@ApplicationService
@AllArgsConstructor
public class FlowControlQuery {

    private final FlowControlRepository flowControlRepository;

    public FlowControlOutputDto getFlowControl(final @NonNull UUID id) {
        final FlowControl flowControl = flowControlRepository.getOneByAggregateId(id);
        return flowControl.toDto();
    }

    public List<GpsPointDto> getGpsPointsList(final @NonNull UUID flowControlId) {
        return flowControlRepository.getOneByAggregateId(flowControlId).getGpsPoints();
    }

}
