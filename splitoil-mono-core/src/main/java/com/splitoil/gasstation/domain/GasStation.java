package com.splitoil.gasstation.domain;

import com.splitoil.shared.AbstractEntity;
import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.HALF_UP;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GasStation extends AbstractEntity {

    static final RoundingMode RATING_ROUNDING = HALF_UP;

    static final BigDecimal NO_RATING = BigDecimal.ZERO;

    @Getter
    private GasStationId gasStation;

    @Builder.Default
    @Getter
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    private Long numberOfRatings = 0L;

    void addRating(final Rating newRating) {
        rating = rating
            .multiply(new BigDecimal(numberOfRatings))
            .add(new BigDecimal(newRating.getRating()))
            .setScale(2, RATING_ROUNDING)
            .divide(new BigDecimal(++numberOfRatings), RATING_ROUNDING);

    }

    BigDecimal getOverallRating() {
        return rating;
    }

}
