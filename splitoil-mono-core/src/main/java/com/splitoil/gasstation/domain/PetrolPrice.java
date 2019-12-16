package com.splitoil.gasstation.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.UUID;

@ToString(exclude = "uuid")
@EqualsAndHashCode(of = "uuid")
class PetrolPrice {

    static final Comparator<PetrolPrice> SORT_BY_NEWEST_FIRST = Comparator.comparing(PetrolPrice::getCreated).reversed();

    private UUID uuid = UUID.randomUUID();

    private BigDecimal amount;

    private Currency currency;

    @Getter
    private PetrolType petrolType;

    private PetrolPriceStatus status;

    @Getter(value = AccessLevel.PACKAGE)
    private Instant created = Instant.now();

    private PetrolPrice(BigDecimal amount, Currency currency, PetrolType petrolType, PetrolPriceStatus status) {
        this.amount = amount;
        this.currency = currency;
        this.petrolType = petrolType;
        this.status = status;
    }

    static PetrolPrice of(BigDecimal amount, Currency currency, PetrolType petrolType) {
        return new PetrolPrice(amount, currency, petrolType, PetrolPriceStatus.PENDING);
    }

    void setAccepted() {
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
}
