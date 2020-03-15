package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.GasStationIdDto;
import com.splitoil.shared.AbstractAggregateRoot;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ObservedGasStation extends AbstractAggregateRoot {

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

    GasStationIdDto toDto() {
        return gasStationId.toDto();
    }
}
