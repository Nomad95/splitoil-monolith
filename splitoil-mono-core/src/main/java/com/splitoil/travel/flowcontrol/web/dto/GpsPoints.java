package com.splitoil.travel.flowcontrol.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
@Getter
@AllArgsConstructor(staticName = "of")
public class GpsPoints {
    List<GpsPointDto> gpsPoints;
}
