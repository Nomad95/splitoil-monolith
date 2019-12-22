package com.splitoil.gasstation.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
public class AddToObservableDto {

    private GasStationIdDto gasStationId;

    private DriverDto driver;

}
