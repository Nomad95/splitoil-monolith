package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarDto {
    @NonNull private UUID id;
    @NonNull private int seatsOccupied;
}
