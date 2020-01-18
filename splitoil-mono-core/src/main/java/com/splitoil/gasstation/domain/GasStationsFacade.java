package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.*;
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
@RequiredArgsConstructor
public class GasStationsFacade {

    private final ObservedGasStationsRepository observedGasStationsRepository;

    private final GasStationRepository gasStationRepository;

    private final GasStationCreator gasStationCreator;

    private final PetrolPriceRepository petrolPriceRepository;

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
        final PetrolPrice petrolPrice = gasStationCreator.createPetrolPrice(command);
        petrolPriceRepository.save(petrolPrice);

        return petrolPrice.getUuid();
    }

    public void acceptPetrolPrice(final AcceptPetrolPriceDto command) {
        final PetrolPrice petrolPrice = petrolPriceRepository.findByUuid(command.getPriceUuid()).orElseThrow(GasPriceNotFoundException::new);
        petrolPrice.setAccepted();
    }

    public BigDecimal getCurrentPetrolPrice(final GetPetrolPriceDto query) {
        final GasStationId gasStationId = gasStationCreator.createGasStationId(query.getGasStationIdDto());
        final PetrolType petrolType = PetrolType.valueOf(query.getPetrolType());
        final Currency currency = Currency.valueOf(query.getCurrency());

        final List<PetrolPrice> lastPrice = petrolPriceRepository.getLatestPrice(gasStationId, petrolType, currency, PageRequest.of(0, 1))
            .getContent();

        if (lastPrice.isEmpty())
            return BigDecimal.ZERO;

        return lastPrice.get(0).getPetrolPrice();
    }

    public GasStationDto showGasStationBrief(final GasStationId gasStationId, final Currency currency) {
        final BigDecimal rating = getRatingIfGasStationExist(gasStationId);
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

    private BigDecimal getRatingIfGasStationExist(final GasStationId gasStationId) {
        final Optional<GasStation> gasStationOptional = gasStationRepository.findOptionalByGasStation(gasStationId);

        if (gasStationOptional.isPresent()) {
            return gasStationOptional.get().getRating();
        }

        return BigDecimal.ZERO;
    }
}
