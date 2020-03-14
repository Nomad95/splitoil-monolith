package com.splitoil.car.domain;

import com.splitoil.gasstation.dto.GasStationIdDto;
import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GasStation extends AbstractValue {

    @NotNull
    @Embedded
    private GeoPoint location;

    @NotBlank
    private String name;

    static GasStation from(final GasStationIdDto dto) {
        return new GasStation(GeoPoint.of(dto.getLocation().getLon(), dto.getLocation().getLat()), dto.getName());
    }

}
