package com.splitoil.car.domain;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Driver extends AbstractValue {

    private Long driverId;

}
