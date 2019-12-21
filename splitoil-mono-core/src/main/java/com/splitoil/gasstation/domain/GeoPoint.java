package com.splitoil.gasstation.domain;

import com.splitoil.gasstation.dto.Geo;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
class GeoPoint implements Geo {

    private double lon;

    private double lat;
}
