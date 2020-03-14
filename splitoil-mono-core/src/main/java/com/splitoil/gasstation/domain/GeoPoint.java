package com.splitoil.gasstation.domain;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
class GeoPoint extends AbstractValue {

    private double lon;

    private double lat;
}
