package com.splitoil.travel.flowcontrol.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddWaypointReachedGpsPointCommand {
    @NonNull private UUID flowControlId;
    @NonNull private GeoPointDto geoPoint;
    @NonNull private UUID waypointId;
    @NonNull private String waypointType;
}
