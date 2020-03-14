package com.splitoil.car.domain;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FuelTank extends AbstractValue {

    @Enumerated(value = EnumType.STRING)
    private PetrolType petrolType;

    private BigDecimal capacity;
}
