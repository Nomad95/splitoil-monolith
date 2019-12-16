package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class GasStationDto {

    @Singular
    private List<PetrolPriceDto> petrolPrices;

    private BigDecimal stationRate;

    public List<PetrolPriceDto> getPetrolPrices() {
        return List.copyOf(petrolPrices);
    }

}
