package com.splitoil.gasstation.domain;

import com.splitoil.shared.AbstractEntity;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ObservedGasStation extends AbstractEntity {

    @Embedded
    private GasStationId gasStationId;

    @Embedded
    private Driver observer;

    static ObservedGasStation from(final GasStationId gasStationId, final Driver driver) {
        return ObservedGasStation.builder()
            .gasStationId(gasStationId)
            .observer(driver)
            .build();
    }
}
