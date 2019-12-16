package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.GasStationDto;
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

    public void addToObservables(final GasStationId gasStationId, final Driver driver) {
        observedGasStationsRepository.addGasStationToObservables(ObservedGasStation.from(gasStationId, driver));
    }

    public List<GasStationId> getObservedGasStations(final Driver driver) {
        return observedGasStationsRepository.findByDriver(driver).stream()
            .map(ObservedGasStation::getGasStationId)
            .collect(toUnmodifiableList());
    }

    public void rateGasStation(final GasStationId gasStationId, final Rating rating) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOne(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().addRating(rating);
        } else {
            final GasStation gasStation = gasStationCreator.create(gasStationId);
            gasStation.addRating(rating);
            gasStationRepository.save(gasStation);
        }
    }

    public BigDecimal getRating(final GasStationId gasStationId) {
        return gasStationRepository.findOne(gasStationId)
            .map(GasStation::getOverallRating)
            .orElse(BigDecimal.ZERO);
    }

    public void addPetrolPrice(final GasStationId gasStationId, final PetrolPrice petrolPrice) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOne(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().addPetrolPrice(petrolPrice);
        } else {
            final GasStation gasStation = gasStationCreator.create(gasStationId);
            gasStation.addPetrolPrice(petrolPrice);
            gasStationRepository.save(gasStation);
        }
    }

    public void acceptPetrolPrice(final GasStationId gasStationId, final PetrolPrice petrolPrice) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOne(gasStationId);
        if (gasStationOptional.isPresent()) {
            gasStationOptional.get().acceptPetrolPrice(petrolPrice);
        } else {
            throw new EntityNotFoundException("Gas station " + gasStationId + " doesnt exist");
        }
    }

    public BigDecimal getCurrentPetrolPrice(final GasStationId gasStationId, final PetrolType petrolType, final Currency currency) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOne(gasStationId);
        if (gasStationOptional.isPresent()) {
            return gasStationOptional.get().getCurrentPetrolPrice(petrolType, currency);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public GasStationDto showGasStationBrief(final GasStationId gasStationId, final Currency currency) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOne(gasStationId);
        if (gasStationOptional.isPresent()) {
            return gasStationOptional.get().toDtoWithCurrency(currency);
        } else {
            return gasStationCreator.createEmptyGasStationDtoView();
        }
    }
}
