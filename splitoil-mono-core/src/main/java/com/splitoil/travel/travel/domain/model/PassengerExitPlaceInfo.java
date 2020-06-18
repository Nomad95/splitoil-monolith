package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.travel.web.dto.waypoint.ExitPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PassengerExitPlaceInfo extends WaypointAdditionalInfo {
    private UUID passengerId;
    private UUID carId;

    @Override
    WaypointAdditionalInfoPayload toDto() {
        return new ExitPlaceInfoDto(passengerId, carId);
    }
}
