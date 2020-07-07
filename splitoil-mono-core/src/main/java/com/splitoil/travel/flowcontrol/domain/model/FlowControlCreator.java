package com.splitoil.travel.flowcontrol.domain.model;

import com.splitoil.travel.flowcontrol.application.command.CreateFlowControlCommand;
import com.splitoil.travel.flowcontrol.web.dto.GeoPointDto;
import com.splitoil.travel.travel.domain.model.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
public class FlowControlCreator {

    public FlowControl createNewFlowControl(final @NonNull CreateFlowControlCommand createFlowControlCommand) {
        return new FlowControl(createFlowControlCommand.getCarId(), createFlowControlCommand.getTravelId());
    }

    public GpsPoint createGpsPoint(final @NonNull GeoPointDto geoPoint) {
        final GeoPoint point = GeoPoint.of(geoPoint.getLon(), geoPoint.getLat());
        return new GpsPoint(point, Instant.now());
    }

    public GpsPoint createGpsPoint(final @NonNull GeoPointDto geoPoint, final @NonNull UUID waypointId, final @NonNull String waypointType) {
        final GeoPoint point = GeoPoint.of(geoPoint.getLon(), geoPoint.getLat());
        final Waypoint waypoint = new Waypoint(waypointId, waypointType);
        return new GpsPoint(point, Instant.now(), waypoint);
    }
}
