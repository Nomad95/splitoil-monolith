package com.splitoil.travel.travel.web.dto;

import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaypointDto {
    private UUID id;
    private GeoPointDto location;
    private String waypointType;
    private boolean historical;
    private WaypointAdditionalInfoPayload additionalInfo;
}
