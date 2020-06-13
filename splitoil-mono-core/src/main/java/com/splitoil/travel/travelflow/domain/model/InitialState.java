package com.splitoil.travel.travelflow.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.travelflow.web.dto.CarStateOutputDto;
import com.splitoil.travel.travelflow.web.dto.StateOutputDto;
import lombok.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@JsonDeserialize(builder = InitialState.InitialStateBuilder.class)
class InitialState implements JsonEntity, Serializable {

    @Builder.Default
    private Map<UUID, CarState> carStates = new HashMap<>();

    List<UUID> getStatedCars() {
        return new ArrayList<>(carStates.keySet());
    }

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

    public StateOutputDto toDto() {
        final List<CarStateOutputDto> cars = carStates.entrySet().stream()
            .map(e -> CarStateOutputDto.builder().carId(e.getKey()).fuelAmount(e.getValue().getFuelLevel()).odometer(e.getValue().getOdometer()).build())
            .collect(
                Collectors.toUnmodifiableList());

        return StateOutputDto.builder()
            .carsState(cars)
            .build();
    }
}