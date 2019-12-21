package com.splitoil.gasstation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class GeoPointInputDto implements Geo {

    private double lon;

    private double lat;

    @JsonCreator
    public GeoPointInputDto(
        @JsonProperty("lon") final double lon,
        @JsonProperty("lat") final double lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
