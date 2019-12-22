package com.splitoil.gasstation.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode
@Value(staticConstructor = "of")
public class GasStationIdDto {

    private GeoPointDto location;

    private String name;
}
