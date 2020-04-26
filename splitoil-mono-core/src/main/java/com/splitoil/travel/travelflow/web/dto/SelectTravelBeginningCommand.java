package com.splitoil.travel.travelflow.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectTravelBeginningCommand {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
}
