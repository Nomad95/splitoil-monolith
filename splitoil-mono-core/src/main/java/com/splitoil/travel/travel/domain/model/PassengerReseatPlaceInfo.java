package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.travel.web.dto.waypoint.ReseatPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PassengerReseatPlaceInfo extends WaypointAdditionalInfo {
    private UUID passengerId;
    private UUID carFrom;
    private UUID carTo;

    @Override
    WaypointAdditionalInfoPayload toDto() {
        return new ReseatPlaceInfoDto(passengerId, carFrom, carTo);
    }
}
