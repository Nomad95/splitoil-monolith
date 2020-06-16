package com.splitoil.travel.travel.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetCarInitialStateCommand {
    @NonNull private UUID travelId;
    @NonNull private UUID carId;
    @NonNull private BigDecimal currentFuelLevel;
    private int odometer;
}
