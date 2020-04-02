package com.splitoil.car.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelEndedEvent {

    @NotNull
    private UUID carId;

    @NotNull
    private Instant dateStarted;

    @NotNull
    private Instant dateEnded;

    @NotNull
    private Long travelLength;

}
