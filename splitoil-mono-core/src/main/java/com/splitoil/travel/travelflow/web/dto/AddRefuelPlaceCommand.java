package com.splitoil.travel.travelflow.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddRefuelPlaceCommand {
    @NonNull private UUID travelId;
    @NonNull private GeoPointDto location;
    @NonNull private BigDecimal cost;
    @NonNull private BigDecimal fuelAmountInLitres;

}
