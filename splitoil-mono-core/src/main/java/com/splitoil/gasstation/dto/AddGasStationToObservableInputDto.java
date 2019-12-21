package com.splitoil.gasstation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value(staticConstructor = "of")
public class AddGasStationToObservableInputDto implements AddToObservableCommand {

    @NotNull
    private GeoPointInputDto geoPoint;

    @NotBlank
    private String gasStationName;

    @NotNull
    private Long driverId;

    @JsonCreator
    public AddGasStationToObservableInputDto(
        @JsonProperty("geoPoint") final GeoPointInputDto geoPoint,
        @JsonProperty("gasStationName") final String gasStationName,
        @JsonProperty("driverId") final Long driverId) {
        this.geoPoint = geoPoint;
        this.gasStationName = gasStationName;
        this.driverId = driverId;
    }
}
