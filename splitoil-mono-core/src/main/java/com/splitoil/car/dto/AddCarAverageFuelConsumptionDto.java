package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddCarAverageFuelConsumptionDto {

    private Long carId;

    private BigDecimal avgFuelConsumption;

}
