package com.splitoil.travel.travelflow.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelOutputDto {
    @NonNull private UUID travelId;
}
