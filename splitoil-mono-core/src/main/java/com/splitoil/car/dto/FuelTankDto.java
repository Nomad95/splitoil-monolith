package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelTankDto {

    private Long carId;

    private String fuelType;

    private BigDecimal capacity;

}
