package com.splitoil.gasstation.domain;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GasStationId {

    @Embedded
    private GeoPoint location;

    private String name;
}
