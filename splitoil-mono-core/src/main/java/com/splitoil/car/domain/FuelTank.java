package com.splitoil.car.domain;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Getter
@Embeddable
@EqualsAndHashCode
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelTank {

    @Enumerated(value = EnumType.STRING)
    private PetrolType petrolType;

    private BigDecimal capacity;
}
