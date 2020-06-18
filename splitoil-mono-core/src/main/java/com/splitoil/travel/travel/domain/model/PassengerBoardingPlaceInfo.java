package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.travel.web.dto.waypoint.BoardingPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PassengerBoardingPlaceInfo extends WaypointAdditionalInfo {
    private UUID passengerId;
    private UUID carId;

    @Override
    WaypointAdditionalInfoPayload toDto() {
        return new BoardingPlaceInfoDto(passengerId, carId);
    }
}
