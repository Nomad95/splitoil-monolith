package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@EqualsAndHashCode
public class AcceptPetrolPriceDto {

    private GasStationIdDto gasStationIdDto;

    private UUID priceUuid;

    private String petrolType;

}
