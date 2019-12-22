package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.*;

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

    GasStationId createGasStationId(final AddToObservableDto command) {
        return GasStationId.from(command.getGasStationId());
    }

    Driver createDriver(final AddToObservableDto command) {
        return Driver.from(command.getDriver());
    }

    ObservedGasStation createObservedGasStation(final AddToObservableDto command) {
        return ObservedGasStation.from(createGasStationId(command), createDriver(command));
    }

    GasStationId createGasStationId(final AddRatingDto command) {
        return GasStationId.from(command.getGasStationId());
    }

    Rating createRating(final int rating) {
        return Rating.of(rating);
    }

    GasStationId createGasStationId(final GasStationIdDto gasStationIdDto) {
        return GasStationId.from(gasStationIdDto);
    }
}
