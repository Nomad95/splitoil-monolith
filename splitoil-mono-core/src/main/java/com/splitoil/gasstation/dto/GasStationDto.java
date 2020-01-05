package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class GasStationDto extends RepresentationModel<GasStationDto> {

    @Singular
    private List<PetrolPriceDto> petrolPrices;

    private BigDecimal stationRate;

    public List<PetrolPriceDto> getPetrolPrices() {
        return List.copyOf(petrolPrices);
    }

}
