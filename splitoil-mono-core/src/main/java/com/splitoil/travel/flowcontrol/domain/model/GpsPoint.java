package com.splitoil.travel.flowcontrol.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.flowcontrol.web.dto.GpsPointDto;
import com.splitoil.travel.travel.domain.model.GeoPoint;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class GpsPoint implements JsonEntity {

    @NonNull
    private GeoPoint geoPoint;

    @NonNull
    private Instant signalReceivedTime;

    @Nullable
    private Waypoint associatedWaypoint;

    GpsPoint(@NonNull final GeoPoint geoPoint, @NonNull final Instant signalReceivedTime) {
        this.geoPoint = geoPoint;
        this.signalReceivedTime = signalReceivedTime;
    }

    GpsPointDto toDto() {
        return GpsPointDto.builder()
            .associatedWaypointId(Objects.isNull(associatedWaypoint) ? null : associatedWaypoint.getWaypointUuid())
            .waypointType(Objects.isNull(associatedWaypoint) ? null : associatedWaypoint.getWaypointType())
            .signalReceivedTime(signalReceivedTime)
            .geoPoint(geoPoint.toDto())
            .build();
    }
}
