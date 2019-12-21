package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

@Transactional
@RequiredArgsConstructor
public class GasStationsFacade {

    private final ObservedGasStationsRepository observedGasStationsRepository;

    private final GasStationRepository gasStationRepository;

    private final GasStationCreator gasStationCreator;

    public void addToObservables(final AddToObservableCommand command) {
        final var gasStationId = new GasStationId(GeoPoint.of(
            command.getGeoPoint().getLon(),
            command.getGeoPoint().getLat()),
            command.getGasStationName());
        final var driver = new Driver(command.getDriverId());

        observedGasStationsRepository.save(ObservedGasStation.from(gasStationId, driver));
    }

    public List<ObservedGasStationOutput> getObservedGasStations(final Long driverId) {
        return observedGasStationsRepository.findAllByObserver(new Driver(driverId)).stream()
            .map(gs -> ObservedGasStationOutputDto.builder()
                .location(new GeoPointInputDto(
                    gs.getGasStationId().getLocation().getLon(),
                    gs.getGasStationId().getLocation().getLat()))
                .name(gs.getGasStationId().getName()).build())
            .collect(toUnmodifiableList());
    }

    public void rateGasStation(final GasStationId gasStationId, final Rating rating) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().addRating(rating);
        } else {
            final GasStation gasStation = gasStationCreator.create(gasStationId);
            gasStation.addRating(rating);
            gasStationRepository.save(gasStation);
        }
    }

    public BigDecimal getRating(final GasStationId gasStationId) {
        return gasStationRepository.findOptionalByGasStation(gasStationId)
            .map(GasStation::getOverallRating)
            .orElse(BigDecimal.ZERO);
    }

    public void addPetrolPrice(final GasStationId gasStationId, final PetrolPrice petrolPrice) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().addPetrolPrice(petrolPrice);
        } else {
            final GasStation gasStation = gasStationCreator.create(gasStationId);
            gasStation.addPetrolPrice(petrolPrice);
            gasStationRepository.save(gasStation);
        }
    }

    public void acceptPetrolPrice(final GasStationId gasStationId, final PetrolPrice petrolPrice) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().acceptPetrolPrice(petrolPrice);
        } else {
            throw new EntityNotFoundException("Gas station " + gasStationId + " doesnt exist");
        }
    }

    public BigDecimal getCurrentPetrolPrice(final GasStationId gasStationId, final PetrolType petrolType, final Currency currency) {
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
