package com.splitoil.car.domain;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Driver extends AbstractValue {

    private UUID driverId;

}
