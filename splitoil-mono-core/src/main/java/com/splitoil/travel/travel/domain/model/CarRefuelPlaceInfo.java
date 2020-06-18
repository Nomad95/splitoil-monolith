package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.travel.web.dto.waypoint.RefuelPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CarRefuelPlaceInfo extends WaypointAdditionalInfo {
    private UUID carBeingRefueled;

    @Override
    WaypointAdditionalInfoPayload toDto() {
        return new RefuelPlaceInfoDto(carBeingRefueled);
    }
}
