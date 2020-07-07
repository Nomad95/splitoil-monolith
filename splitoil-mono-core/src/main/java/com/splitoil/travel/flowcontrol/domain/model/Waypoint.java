package com.splitoil.travel.flowcontrol.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class Waypoint implements JsonEntity {

    private UUID waypointUuid;

    private String waypointType;
}
