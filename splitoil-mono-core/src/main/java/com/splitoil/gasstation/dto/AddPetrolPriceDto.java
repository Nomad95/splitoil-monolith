package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
@Builder
@EqualsAndHashCode
public class AddPetrolPriceDto {

    @NonNull
    private GasStationIdDto gasStationIdDto;

    @Positive
    private BigDecimal amount;

    @NonNull
    private String currency;

    @NonNull
    private String petrolType;

}
