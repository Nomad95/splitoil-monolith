package com.splitoil.travel.travel.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelOutputDto {
    @NonNull private UUID travelId;
    @NonNull private String travelStatus;
    @NonNull private StateOutputDto state;
}
