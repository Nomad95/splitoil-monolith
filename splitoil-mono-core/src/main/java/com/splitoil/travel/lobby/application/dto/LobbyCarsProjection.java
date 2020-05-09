package com.splitoil.travel.lobby.application.dto;

import com.splitoil.travel.lobby.domain.model.Car;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
public class LobbyCarsProjection {
    Map<UUID, Car> cars;
}
