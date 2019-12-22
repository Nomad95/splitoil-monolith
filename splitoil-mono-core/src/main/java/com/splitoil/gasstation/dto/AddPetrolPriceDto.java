package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@EqualsAndHashCode
public class AddPetrolPriceDto {

    private GasStationIdDto gasStationIdDto;

    private BigDecimal amount;

    private String currency;

    private String petrolType;

}
