package com.splitoil.gasstation.domain;

import com.splitoil.shared.AbstractEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embedded;

@Getter
@Builder
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
