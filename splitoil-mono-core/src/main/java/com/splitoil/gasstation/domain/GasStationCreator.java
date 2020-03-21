package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.*;
import com.splitoil.shared.model.Currency;

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

    Driver createDriver(final AddToObservableDto command) {
        return Driver.from(command.getDriver());
    }

    ObservedGasStation createObservedGasStation(final AddToObservableDto command) {
        return ObservedGasStation.from(createGasStationId(command.getGasStationId()), createDriver(command));
    }

    Rating createRating(final int rating) {
        return Rating.of(rating);
    }

    GasStationId createGasStationId(final GasStationIdDto gasStationIdDto) {
        return GasStationId.from(gasStationIdDto);
    }

    PetrolPrice createPetrolPrice(final AddPetrolPriceDto addPetrolPriceCommand) {
        return PetrolPrice.of(createGasStationId(addPetrolPriceCommand.getGasStationIdDto()), addPetrolPriceCommand.getAmount(),
            Currency.valueOf(addPetrolPriceCommand.getCurrency()), PetrolType.valueOf(addPetrolPriceCommand.getPetrolType()));
    }
}
