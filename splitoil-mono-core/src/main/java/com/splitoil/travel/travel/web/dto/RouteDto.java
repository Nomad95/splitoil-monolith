package com.splitoil.travel.travel.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteDto {
    @Singular
    @NonNull
    private List<WaypointDto> waypoints;
}
