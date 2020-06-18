package com.splitoil.travel.travel.web.dto.waypoint;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface WaypointAdditionalInfoAware {

    @JsonIgnore
    default WaypointAdditionalInfoPayload getAdditionalInfo() {
        return EmptyInfoDto.INSTANCE;
    }
}
