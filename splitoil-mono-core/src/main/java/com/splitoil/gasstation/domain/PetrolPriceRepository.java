package com.splitoil.gasstation.domain;

import com.splitoil.shared.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
interface PetrolPriceRepository extends CrudRepository<PetrolPrice, Long> {

    Optional<PetrolPrice> findByAggregateId(final UUID uuid);

    @Query("SELECT pp FROM PetrolPrice pp WHERE pp.gasStationId = :gasStationId AND pp.petrolType = :petrolType AND pp.currency = :currency AND pp.status = 'ACCEPTED' "
               + "ORDER BY pp.created DESC")
    Page<PetrolPrice> getLatestPrice(
        @Param("gasStationId") GasStationId gasStationId,
        @Param("petrolType") PetrolType petrolType,
        @Param("currency") Currency currency,
        Pageable pageable);

    Stream<PetrolPrice> findAllByGasStationIdAndCurrencyOrderByCreatedDesc(
        @Param("gasStationId") GasStationId gasStationId,
        @Param("currency") Currency currency);
}
