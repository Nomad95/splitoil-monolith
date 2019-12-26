package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.GasStationDto;
import com.splitoil.gasstation.dto.PetrolPriceDto;
import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GasStation extends AbstractEntity {

    static final RoundingMode ROUNDING_MODE = HALF_UP;

    @Getter
    private GasStationId gasStation;

    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    private Long numberOfRatings = 0L;

    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.gasstation.domain.GasPrices"),
          })
    @Builder.Default
    private GasPrices gasPrices = GasPrices.newInstance();

    void addRating(final Rating newRating) {
        rating = rating
            .multiply(new BigDecimal(numberOfRatings))
            .add(new BigDecimal(newRating.getRating()))
            .setScale(2, ROUNDING_MODE)
            .divide(new BigDecimal(++numberOfRatings), ROUNDING_MODE);
    }

    BigDecimal getOverallRating() {
        return rating;
    }

    void addPetrolPrice(final @NonNull PetrolPrice petrolPrice) {
        gasPrices.add(petrolPrice);
    }

    void acceptPetrolPrice(final UUID petrolPriceId) {
        gasPrices.accept(petrolPriceId);
    }

    BigDecimal getCurrentPetrolPrice(final PetrolType petrolType, final Currency currency) {
        return gasPrices.getCurrentPetrolPrice(petrolType, currency);
    }

    //TODO: defaultCurrency
    GasStationDto toDtoWithCurrency(final Currency currency) {
        final GasStationDto.GasStationDtoBuilder gasStationDtoBuilder = GasStationDto.builder().stationRate(rating);
        for (final PetrolType petrolType : PetrolType.values()) {
            gasStationDtoBuilder.petrolPrice(new PetrolPriceDto(petrolType.name(), gasPrices.getCurrentPetrolPrice(petrolType, currency)));
        }

        return gasStationDtoBuilder.build();
    }
}
