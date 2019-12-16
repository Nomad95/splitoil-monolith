package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.GasStationDto;
import com.splitoil.gasstation.dto.PetrolPriceDto;

import java.math.BigDecimal;

class GasStationCreator {

    GasStation create(final GasStationId gasStation) {
        return GasStation.builder().gasStation(gasStation).build();
    }

    GasStationDto createEmptyGasStationDtoView() {
        final GasStationDto.GasStationDtoBuilder gasStationDtoBuilder = GasStationDto.builder().stationRate(BigDecimal.ZERO);
        for (final PetrolType petrolType : PetrolType.values()) {
            gasStationDtoBuilder.petrolPrice(new PetrolPriceDto(petrolType.name(), BigDecimal.ZERO));
        }

        return gasStationDtoBuilder.build();
    }
}
