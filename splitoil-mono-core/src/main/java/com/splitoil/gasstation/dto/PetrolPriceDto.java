package com.splitoil.gasstation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PetrolPriceDto {

    private final String petrolType;

    private final BigDecimal value;
}
