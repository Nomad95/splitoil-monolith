package com.splitoil.gasstation.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode
public class GeoPointDto {

    private double lon;
    private double lat;
}
