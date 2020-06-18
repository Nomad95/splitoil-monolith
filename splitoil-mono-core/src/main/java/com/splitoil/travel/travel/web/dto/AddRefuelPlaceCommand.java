package com.splitoil.travel.travel.web.dto;

import com.splitoil.travel.travel.web.dto.waypoint.RefuelPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoAware;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddRefuelPlaceCommand implements WaypointAdditionalInfoAware {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
    @NonNull private UUID carBeingRefueld;

    @Override
    public WaypointAdditionalInfoPayload getAdditionalInfo() {
        return new RefuelPlaceInfoDto(carBeingRefueld);
    }
}
