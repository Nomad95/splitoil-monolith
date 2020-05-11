package com.splitoil.travel.travelflow.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.splitoil.infrastructure.json.JsonEntity;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonDeserialize(builder = InitialState.InitialStateBuilder.class)
class InitialState implements JsonEntity, Serializable {

    @Builder.Default
    private Map<UUID, CarState> carStates = new HashMap<>();

    void addCarState(final UUID carId, final CarState carState) {
        carStates.put(carId, carState);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class InitialStateBuilder {
        private Map<UUID, CarState> carStates = new HashMap<>();

        public InitialStateBuilder participant(final UUID carId, final CarState carState) {
            carStates.put(carId, carState);
            return this;
        }
    }
}