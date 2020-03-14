package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.DriverDto;
import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Driver extends AbstractValue {

    private Long driverId;

    static Driver from(final DriverDto dto) {
        return new Driver(dto.getDriverId());
    }
}
