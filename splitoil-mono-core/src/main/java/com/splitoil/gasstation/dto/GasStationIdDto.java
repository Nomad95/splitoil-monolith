package com.splitoil.gasstation.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class GasStationIdDto extends RepresentationModel<GasStationIdDto> {

    @NonNull
    private GeoPointDto location;

    @NonNull
    private String name;
}
