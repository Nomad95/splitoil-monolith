package com.splitoil.travel.travel.web.dto;

import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoAware;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStopPlaceCommand implements WaypointAdditionalInfoAware {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
}
