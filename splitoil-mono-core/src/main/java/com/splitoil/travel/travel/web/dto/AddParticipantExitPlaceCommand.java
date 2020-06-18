package com.splitoil.travel.travel.web.dto;

import com.splitoil.travel.travel.web.dto.waypoint.ExitPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoAware;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddParticipantExitPlaceCommand implements WaypointAdditionalInfoAware {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
    @NonNull private UUID passengerId;
    @NonNull private UUID carId;

    @Override
    public WaypointAdditionalInfoPayload getAdditionalInfo() {
        return new ExitPlaceInfoDto(passengerId, carId);
    }
}
