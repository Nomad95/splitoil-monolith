package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
public class GetPetrolPriceDto {

    private GasStationIdDto gasStationIdDto;

    private String currency;

    private String petrolType;

}
