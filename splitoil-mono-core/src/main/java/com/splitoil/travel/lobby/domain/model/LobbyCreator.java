package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand;

import java.util.UUID;

public class LobbyCreator {

    public Lobby createNewLobby(final CreateLobbyCommand createLobbyCommand) {
        //TODO: currency!
        return new Lobby(createLobbyCommand.getName(), createDriver(createLobbyCommand.getDriverId()), Currency.PLN);
    }

    public Driver createDriver(final Long driverId) {
        return Driver.of(driverId);
    }

    public CarId createCar(final UUID carId) {
        return CarId.of(carId);
    }
}
