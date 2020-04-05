package com.splitoil.travel.lobby.application;

import com.splitoil.car.domain.CarFacade;
import com.splitoil.car.dto.CarFullDto;
import com.splitoil.shared.annotation.AntiCorruptionLayer;
import com.splitoil.travel.lobby.domain.model.Car;
import com.splitoil.travel.lobby.domain.model.LobbyCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AntiCorruptionLayer
@AllArgsConstructor
public class CarTranslationService {

    private final CarFacade carFacade;

    private final LobbyCreator lobbyCreator;

    public Car getCar(final UUID carId) {
        final CarFullDto carDto = carFacade.getOneCar(carId);
        return lobbyCreator.createCar(carDto);
    }

}
