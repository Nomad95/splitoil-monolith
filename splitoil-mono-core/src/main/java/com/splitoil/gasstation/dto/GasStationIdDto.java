package com.splitoil.gasstation.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class GasStationIdDto {

    @NonNull
    private GeoPointDto location;

    @NonNull
    private String name;
}
