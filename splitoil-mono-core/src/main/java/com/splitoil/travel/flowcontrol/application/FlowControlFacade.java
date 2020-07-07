package com.splitoil.travel.flowcontrol.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.flowcontrol.application.command.CreateFlowControlCommand;
import com.splitoil.travel.flowcontrol.domain.event.WaypointReached;
import com.splitoil.travel.flowcontrol.domain.model.*;
import com.splitoil.travel.flowcontrol.web.dto.AddGpsPointCommand;
import com.splitoil.travel.flowcontrol.web.dto.AddWaypointReachedGpsPointCommand;
import com.splitoil.travel.flowcontrol.web.dto.FlowControlOutputDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.splitoil.travel.flowcontrol.domain.model.Result.REDUNDANT;

@Transactional
@ApplicationService
@AllArgsConstructor
public class FlowControlFacade {

    private final EventPublisher eventPublisher;

    private final FlowControlRepository flowControlRepository;

    private final FlowControlCreator flowControlCreator;

    public FlowControlOutputDto createFlowControl(final @NonNull CreateFlowControlCommand createFlowControlCommand) {
        final FlowControl newFlowControl = flowControlCreator.createNewFlowControl(createFlowControlCommand);

        flowControlRepository.save(newFlowControl);

        return newFlowControl.toDto();
    }

    public FlowControlOutputDto getFlowControl(final @NonNull UUID id) {
        final FlowControl flowControl = flowControlRepository.getOneByAggregateId(id);
        return flowControl.toDto();
    }

    public void addGpsPoint(final @NonNull AddGpsPointCommand addGpsPointCommand) {
        final FlowControl flowControl = flowControlRepository.getOneByAggregateId(addGpsPointCommand.getFlowControlId());
        final GpsPoint gpsPoint = flowControlCreator.createGpsPoint(addGpsPointCommand.getGeoPoint());

        flowControl.addGpsPoint(gpsPoint);
    }

    public void addWaypointGpsPoint(final @NonNull AddWaypointReachedGpsPointCommand addWaypointReachedGpsPointCommand) {
        final FlowControl flowControl = flowControlRepository.getOneByAggregateId(addWaypointReachedGpsPointCommand.getFlowControlId());
        final GpsPoint gpsPoint = flowControlCreator.createGpsPoint(
            addWaypointReachedGpsPointCommand.getGeoPoint(),
            addWaypointReachedGpsPointCommand.getWaypointId(),
            addWaypointReachedGpsPointCommand.getWaypointType());

        final Result result = flowControl.addGpsPoint(gpsPoint);

        if (REDUNDANT == result) {
            return;
        }

        eventPublisher.publish(new WaypointReached(flowControl.getAggregateId(), flowControl.getCarId(), addWaypointReachedGpsPointCommand.getWaypointId()));
    }
}
