package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.UserCurrencyProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@AllArgsConstructor
public class LobbyCreator {

    private final UserCurrencyProvider userCurrencyProvider;

    public Lobby createNewLobby(final @NonNull String lobbyName, final @NonNull Driver driver) {
        return new Lobby(lobbyName, driver, userCurrencyProvider.getCurrentUserDefaultCurrency());
    }

    public Driver createDriver(final Long driverId) {
        return Driver.of(driverId);
    }

    public CarId createCar(final UUID carId) {
        return CarId.of(carId);
    }
}
