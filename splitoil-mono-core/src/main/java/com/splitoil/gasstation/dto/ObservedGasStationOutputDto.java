package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ObservedGasStationOutputDto implements ObservedGasStationOutput {

    private GeoPointInputDto location;

    private String name;
}
