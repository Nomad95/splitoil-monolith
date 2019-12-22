package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.GasStationIdDto;
import com.splitoil.gasstation.dto.GeoPointDto;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GasStationId {

    @Embedded
    private GeoPoint location;

    private String name;

    static GasStationId from(final GasStationIdDto dto) {
        return new GasStationId(GeoPoint.of(dto.getLocation().getLon(), dto.getLocation().getLat()), dto.getName());
    }

    GasStationIdDto toDto() {
        return GasStationIdDto.of(GeoPointDto.of(location.getLon(), location.getLat()), name);
    }
}
