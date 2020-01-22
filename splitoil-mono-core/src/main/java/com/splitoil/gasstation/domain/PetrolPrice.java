package com.splitoil.gasstation.domain;

import com.splitoil.shared.AbstractEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PetrolPrice extends AbstractEntity {

    static final Comparator<PetrolPrice> SORT_BY_NEWEST_FIRST = Comparator.comparing(PetrolPrice::getCreated).reversed();

    private GasStationId gasStationId;

    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    @Getter
    @Enumerated(value = EnumType.STRING)
    private PetrolType petrolType;

    @Enumerated(value = EnumType.STRING)
    private PetrolPriceStatus status;

    @Builder.Default
    @Getter(value = AccessLevel.PACKAGE)
    private Instant created = Instant.now();

    private PetrolPrice(GasStationId gasStationId, BigDecimal amount, Currency currency, PetrolType petrolType, PetrolPriceStatus status) {
        this.amount = amount;
        this.currency = currency;
        this.petrolType = petrolType;
        this.status = status;
        this.gasStationId = gasStationId;
    }

    static PetrolPrice of(GasStationId gasStationId, BigDecimal amount, Currency currency, PetrolType petrolType) {
        return new PetrolPrice(gasStationId, amount, currency, petrolType, PetrolPriceStatus.PENDING);
    }

    void acceptPrice() {
        this.status = PetrolPriceStatus.ACCEPTED;
    }

    boolean isAccepted() {
        return PetrolPriceStatus.ACCEPTED == status;
    }

    boolean isOfCurrency(final Currency currency) {
        return this.currency == currency;
    }

    BigDecimal getPetrolPrice() {
        return amount;
    }

    boolean isInThisPlace(final @NonNull GasStationId gasStationId) {
        return this.gasStationId.equals(gasStationId);
    }

    boolean isOfPetrolType(final PetrolType petrolType) {
        return this.petrolType == petrolType;
    }
}
