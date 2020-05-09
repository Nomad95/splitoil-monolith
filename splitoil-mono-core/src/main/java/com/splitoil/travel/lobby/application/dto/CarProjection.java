package com.splitoil.travel.lobby.application.dto;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.lobby.domain.model.CarId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarProjection implements JsonEntity{
    @NonNull CarId carId;
}
