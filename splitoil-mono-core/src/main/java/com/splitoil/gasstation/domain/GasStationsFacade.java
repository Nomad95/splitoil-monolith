package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.domain.event.GasStationRated;
import com.splitoil.gasstation.domain.event.PetrolPriceAdded;
import com.splitoil.gasstation.domain.event.StationAddedToObserved;
import com.splitoil.gasstation.dto.*;
import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.model.Currency;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toUnmodifiableList;

@Transactional
@ApplicationService
@RequiredArgsConstructor
public class GasStationsFacade {

    private final ObservedGasStationsRepository observedGasStationsRepository;

    private final GasStationRepository gasStationRepository;

    private final GasStationCreator gasStationCreator;

    private final PetrolPriceRepository petrolPriceRepository;

    private final EventPublisher eventPublisher;

    public GasStationIdDto addToObservables(final @NonNull AddToObservableDto command) {
        final ObservedGasStation observedGasStation = gasStationCreator.createObservedGasStation(command);

        observedGasStationsRepository.save(observedGasStation);
        eventPublisher.publish(new StationAddedToObserved(observedGasStation.getAggregateId(), command.getDriver().getDriverId()));

        return observedGasStation.toDto();
    }

    public List<GasStationIdDto> getObservedGasStations(final @NonNull Long driverId) {
        return observedGasStationsRepository.findAllByObserver(new Driver(driverId)).stream()
            .map(ObservedGasStation::toDto)
            .collect(toUnmodifiableList());
    }

    public BigDecimal rateGasStation(final @NonNull AddRatingDto addRatingToGasStationCommand) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(addRatingToGasStationCommand.getGasStationId());
        final Rating gasStationRating = gasStationCreator.createRating(addRatingToGasStationCommand.getRating());

        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);
        final GasStation gasStation = gasStationOptional.orElseGet(() -> gasStationCreator.create(gasStationId));

        gasStation.addRating(gasStationRating);

        gasStationRepository.save(gasStation);
        eventPublisher.publish(new GasStationRated(gasStation.getAggregateId(), addRatingToGasStationCommand.getRating(), gasStation.getOverallRating()));

        return gasStation.getOverallRating();
    }

    public BigDecimal getRating(final @NonNull GasStationIdDto gasStationIdDto) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(gasStationIdDto);

        return gasStationRepository.findOptionalByGasStation(gasStationId)
            .map(GasStation::getOverallRating)
            .orElse(GasStation.NO_RATING);
    }

    public UUID addPetrolPrice(final @NonNull AddPetrolPriceDto addPetrolPriceToGasStationCommand) {
        final PetrolPrice petrolPrice = gasStationCreator.createPetrolPrice(addPetrolPriceToGasStationCommand);

        petrolPriceRepository.save(petrolPrice);
        eventPublisher.publish(new PetrolPriceAdded(petrolPrice.getAggregateId()));

        return petrolPrice.getAggregateId();
    }

    //Strategia sprawdzania czy cena jest poprawna itp.
    public void acceptPetrolPrice(final @NonNull AcceptPetrolPriceDto command) {
        final PetrolPrice petrolPrice = petrolPriceRepository.findByAggregateId(command.getPriceUuid()).orElseThrow(GasPriceNotFoundException::new);
        petrolPrice.acceptPrice();
    }

    public BigDecimal getCurrentPetrolPrice(final @NonNull GetPetrolPriceDto query) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(query.getGasStationIdDto());
        final PetrolType petrolType = PetrolType.valueOf(query.getPetrolType());
        final Currency currency = Currency.valueOf(query.getCurrency());

        final List<PetrolPrice> lastPrice = petrolPriceRepository.getLatestPrice(gasStationId, petrolType, currency, PageRequest.of(0, 1))
            .getContent();

        if (lastPrice.isEmpty())
            return PetrolPrice.NO_PRICE;

        return lastPrice.get(0).getPetrolPrice();
    }

    public GasStationDto showGasStationBrief(final @NonNull GasStationId gasStationId, final @NonNull Currency currency) {
        final BigDecimal rating = getRatingIfGasStationExistOrZero(gasStationId);
        final List<PetrolPriceDto> prices = petrolPriceRepository.findAllByGasStationIdAndCurrencyOrderByCreatedDesc(gasStationId, currency)
            .filter(PetrolPrice::isAccepted)
            .collect(Collectors.groupingBy(PetrolPrice::getPetrolType))
            .values().stream()
            .filter(v -> !v.isEmpty())
            .map(v -> new PetrolPriceDto(v.get(0).getPetrolType().name(), v.get(0).getPetrolPrice()))
            .collect(Collectors.toList());

        if (prices.isEmpty() && rating.equals(BigDecimal.ZERO)) {
            return gasStationCreator.createEmptyGasStationDtoView();
        }

        return GasStationDto.builder()
            .petrolPrices(prices)
            .stationRate(rating)
            .build();
    }

    private BigDecimal getRatingIfGasStationExistOrZero(final @NonNull GasStationId gasStationId) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);

        if (gasStationOptional.isPresent()) {
            return gasStationOptional.get().getRating();
        }

        return GasStation.NO_RATING;
    }
}
