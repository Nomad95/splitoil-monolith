package com.splitoil.travel.travel.web.dto;

import com.splitoil.travel.travel.web.dto.waypoint.ReseatPlaceInfoDto;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoAware;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddReseatPlaceCommand implements WaypointAdditionalInfoAware {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
    @NonNull private UUID carFrom;
    @NonNull private UUID carTo;
    @NonNull private UUID participantId;

    @Override
    public WaypointAdditionalInfoPayload getAdditionalInfo() {
        return new ReseatPlaceInfoDto(participantId, carFrom, carTo);
    }
}
