package com.splitoil.travel.flowcontrol.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddGpsPointCommand {
    @NonNull private UUID flowControlId;
    @NonNull private GeoPointDto geoPoint;
}
