package com.splitoil.car.domain;

import com.splitoil.shared.AbstractEntity;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class CarRefuel extends AbstractEntity {

    private UUID carId;

    private Instant costDate;

    private BigDecimal value;

    @Embedded
    private GasStation gasStation;

    private BigDecimal fuelAmountInLitres;

    @Enumerated(value = EnumType.STRING)
    private PetrolType petrolType;

}
