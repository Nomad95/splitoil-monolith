package com.splitoil.gasstation.domain;

import com.splitoil.shared.CrudInMemoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class InMemoryPetrolPriceRepository extends CrudInMemoryRepository<PetrolPrice> implements PetrolPriceRepository {

    @Override
    public Page<PetrolPrice> getLatestPrice(final GasStationId gasStationId, final PetrolType petrolType, final com.splitoil.shared.model.Currency currency, final Pageable pageable) {
        final PetrolPrice petrolPrice = map.values().stream()
            .filter(PetrolPrice::isAccepted)
            .filter((v -> v.isInThisPlace(gasStationId)))
            .filter(v -> v.isOfCurrency(currency))
            .filter(v -> v.isOfPetrolType(petrolType))
            .findFirst().orElse(null);

        return new PageImpl<>(Objects.isNull(petrolPrice) ? List.of() : List.of(petrolPrice));
    }

    @Override public Stream<PetrolPrice> findAllByGasStationIdAndCurrencyOrderByCreatedDesc(final GasStationId gasStationId, final com.splitoil.shared.model.Currency currency) {
        return map.values().stream()
            .filter(PetrolPrice::isAccepted)
            .filter((v -> v.isInThisPlace(gasStationId)))
            .filter(v -> v.isOfCurrency(currency));
    }
}
