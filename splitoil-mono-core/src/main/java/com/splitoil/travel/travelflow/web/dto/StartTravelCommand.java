package com.splitoil.travel.travelflow.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartTravelCommand {
    @NonNull private UUID travelId;
}
