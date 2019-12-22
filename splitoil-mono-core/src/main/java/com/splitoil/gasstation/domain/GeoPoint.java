package com.splitoil.gasstation.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
class GeoPoint {

    private double lon;

    private double lat;
}
