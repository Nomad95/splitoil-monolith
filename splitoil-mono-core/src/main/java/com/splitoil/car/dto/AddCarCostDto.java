package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddCarCostDto {

    private UUID carId;

    private Instant date;

    private String name;

    private BigDecimal value;

}
