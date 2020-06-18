package com.splitoil.travel.travel.web.dto.waypoint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class EmptyInfoDto implements WaypointAdditionalInfoPayload {
    public static final EmptyInfoDto INSTANCE = new EmptyInfoDto();
}
