package com.splitoil.travel.travel.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarStateOutputDto {
    private UUID carId;
    private BigDecimal fuelAmount;
    private int odometer;

}
