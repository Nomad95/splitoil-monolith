package com.splitoil.travel.travel.web.dto.waypoint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReseatPlaceInfoDto implements WaypointAdditionalInfoPayload {
    private UUID passengerId;
    private UUID carFrom;
    private UUID carTo;
}
