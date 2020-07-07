package com.splitoil.travel.flowcontrol.web.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GpsPointDto {
    private GeoPointDto geoPoint;
    private Instant signalReceivedTime;
    private UUID associatedWaypointId;
    private String waypointType;
}
