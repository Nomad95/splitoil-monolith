package com.splitoil.travel.travel.domain.model;

import com.splitoil.shared.AbstractValue;
import com.splitoil.travel.flowcontrol.web.dto.GeoPointDto;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class GeoPoint extends AbstractValue implements Serializable {

    private double lon;

    private double lat;

    public GeoPointDto toDto() {
        return GeoPointDto.of(lon, lat);
    }
}
