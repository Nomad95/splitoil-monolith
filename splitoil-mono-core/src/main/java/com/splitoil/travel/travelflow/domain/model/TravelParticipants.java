package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class TravelParticipants implements JsonEntity, Serializable {

    @Singular
    private List<TravelParticipant> participants;

}