package com.splitoil.car.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value
public class CarCostDto {

    String name;

    Instant costDate;

    BigDecimal value;

    UUID carId;

    String currency;
}
