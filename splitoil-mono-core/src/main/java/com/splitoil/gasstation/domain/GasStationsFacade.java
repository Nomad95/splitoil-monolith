package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toUnmodifiableList;

@Transactional
@RequiredArgsConstructor
public class GasStationsFacade {

    private final ObservedGasStationsRepository observedGasStationsRepository;

    private final GasStationRepository gasStationRepository;

    private final GasStationCreator gasStationCreator;

    public GasStationIdDto addToObservables(final AddToObservableDto command) {
        final ObservedGasStation observedGasStation = gasStationCreator.createObservedGasStation(command);

        observedGasStationsRepository.save(observedGasStation);
        return observedGasStation.toDto();
    }

    public List<GasStationIdDto> getObservedGasStations(final Long driverId) {
        return observedGasStationsRepository.findAllByObserver(new Driver(driverId)).stream()
            .map(ObservedGasStation::toDto)
            .collect(toUnmodifiableList());
    }

    public BigDecimal rateGasStation(final AddRatingDto command) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(command.getGasStationId());
        final Rating rating = gasStationCreator.createRating(command.getRating());

        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().addRating(rating);
            return gasStationOptional.get().getOverallRating();
        } else {
            final GasStation gasStation = gasStationCreator.create(gasStationId);
            gasStation.addRating(rating);
            gasStationRepository.save(gasStation);
            return gasStation.getOverallRating();
        }
    }

    public BigDecimal getRating(final GasStationIdDto gasStationIdDto) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(gasStationIdDto);
        return gasStationRepository.findOptionalByGasStation(gasStationId)
            .map(GasStation::getOverallRating)
            .orElse(BigDecimal.ZERO);
    }

    public UUID addPetrolPrice(final AddPetrolPriceDto command) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(command.getGasStationIdDto());
        final PetrolPrice petrolPrice = gasStationCreator.createPetrolPrice(command);

        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().addPetrolPrice(petrolPrice);
        } else {
            final GasStation gasStation = gasStationCreator.create(gasStationId);
            gasStation.addPetrolPrice(petrolPrice);
            gasStationRepository.save(gasStation);
        }

        return petrolPrice.getUuid();
    }

    public void acceptPetrolPrice(final AcceptPetrolPriceDto command) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(command.getGasStationIdDto());

        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().acceptPetrolPrice(command.getPriceUuid());
        } else {
            throw new EntityNotFoundException("Gas station " + gasStationId + " doesnt exist");
        }
    }

    public BigDecimal getCurrentPetrolPrice(final GetPetrolPriceDto query) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(query.getGasStationIdDto());
        final PetrolType petrolType = PetrolType.valueOf(query.getPetrolType());
        final Currency currency = Currency.valueOf(query.getCurrency());

        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            return gasStationOptional.get().getCurrentPetrolPrice(petrolType, currency);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public GasStationDto showGasStationBrief(final GasStationId gasStationId, final Currency currency) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            return gasStationOptional.get().toDtoWithCurrency(currency);
        } else {
            return gasStationCreator.createEmptyGasStationDtoView();
        }
    }
}
