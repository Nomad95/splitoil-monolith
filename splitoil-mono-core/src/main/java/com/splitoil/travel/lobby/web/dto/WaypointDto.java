package com.splitoil.travel.lobby.web.dto;

import com.splitoil.travel.travelflow.web.dto.GeoPointDto;
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
}
