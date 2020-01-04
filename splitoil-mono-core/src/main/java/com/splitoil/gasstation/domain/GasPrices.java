package com.splitoil.gasstation.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.splitoil.infrastructure.json.JsonEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.*;

import static com.splitoil.gasstation.domain.PetrolPrice.SORT_BY_NEWEST_FIRST;

class GasPrices implements JsonEntity {

    private final Map<PetrolType, List<PetrolPrice>> prices;

    @Builder(access = AccessLevel.PACKAGE)
    @JsonCreator
    GasPrices(
        @JsonProperty("prices") final Map<PetrolType, List<PetrolPrice>> prices) {
        this.prices = prices;
    }

    void add(final @NonNull PetrolPrice petrolPrice) {
        prices.computeIfAbsent(petrolPrice.getPetrolType(), k -> new ArrayList<>()).add(petrolPrice);
    }

    void accept(final UUID petrolPriceId) {
        final Optional<PetrolPrice> priceOptional = prices.values().stream()
            .flatMap(List::stream)
            .filter(price -> price.getUuid().equals(petrolPriceId))
            .findFirst();

        if (priceOptional.isPresent()) {
            priceOptional.get().setAccepted();
        } else {
            throw new GasPriceNotFoundException();
        }
    }

    BigDecimal getCurrentPetrolPrice(final PetrolType petrolType, final Currency currency) {
        return prices.get(petrolType).stream()
            .filter(price -> price.isOfCurrency(currency))
            .filter(PetrolPrice::isAccepted)
            .min(SORT_BY_NEWEST_FIRST)
            .map(PetrolPrice::getPetrolPrice)
            .orElse(BigDecimal.ZERO);
    }

    static GasPrices newInstance() {
        final Map<PetrolType, List<PetrolPrice>> pricesMap = new HashMap<>();
        for (final PetrolType value : PetrolType.values()) {
            pricesMap.put(value, new ArrayList<>());
        }
        return GasPrices.builder().prices(pricesMap).build();
    }
}
