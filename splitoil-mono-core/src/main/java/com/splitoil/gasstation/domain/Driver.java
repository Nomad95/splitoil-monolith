package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.DriverDto;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Driver {

    private Long driverId;

    static Driver from(final DriverDto dto) {
        return new Driver(dto.getDriverId());
    }
}
