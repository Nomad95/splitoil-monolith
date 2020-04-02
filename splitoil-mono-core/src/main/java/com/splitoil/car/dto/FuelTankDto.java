package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelTankDto {

    private UUID carId;

    private String fuelType;

    private BigDecimal capacity;

}
