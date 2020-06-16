package com.splitoil.travel.travel.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddReseatPlaceCommand {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
    @NonNull private UUID carFrom;
    @NonNull private UUID carTo;
    @NonNull private UUID participantId;
}
