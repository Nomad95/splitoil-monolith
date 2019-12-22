package com.splitoil.gasstation.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class GasStationIdDto {

    private GeoPointDto location;

    private String name;
}
