package com.splitoil.travel.travel.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class TravelParticipant implements JsonEntity, Serializable {

    private UUID participantId;
    private UUID assignedCarId;
}
