package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
class GeoPoint extends AbstractValue implements Serializable {

    private double lon;

    private double lat;
}
