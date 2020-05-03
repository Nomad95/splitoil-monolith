package com.splitoil.travel.travelflow.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.splitoil.infrastructure.json.JsonEntity;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonDeserialize(builder = TravelParticipants.TravelParticipantsBuilder.class)
class TravelParticipants implements JsonEntity, Serializable {

    @Builder.Default
    private List<TravelParticipant> participants = new ArrayList<>();

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TravelParticipantsBuilder {
        private List<TravelParticipant> participants = new ArrayList<>();

        public TravelParticipantsBuilder participant(final TravelParticipant travelParticipant) {
            participants.add(travelParticipant);
            return this;
        }
    }
}