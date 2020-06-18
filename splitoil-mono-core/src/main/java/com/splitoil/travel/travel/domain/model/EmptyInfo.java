package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.travel.web.dto.waypoint.EmptyInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class EmptyInfo extends WaypointAdditionalInfo {

    @Override
    WaypointAdditionalInfoPayload toDto() {
        return EmptyInfoDto.INSTANCE;
    }


}
