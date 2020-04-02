package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddCarAverageFuelConsumptionDto {

    private UUID carId;

    private BigDecimal avgFuelConsumption;

}
