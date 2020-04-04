package com.splitoil.travel.lobby.domain.model;

import com.splitoil.car.dto.CarFullDto;
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

    public Driver createDriver(final UUID driverId) {
        return Driver.of(driverId);
    }

    public CarId createCarId(final UUID carId) {
        return CarId.of(carId);
    }

    public Participant createPassenger(final UUID id, final String displayName) {
        return Participant.of(id, displayName, ParticipantType.PASSENGER);
    }

    public Participant createAdHocPassenger(final UUID id, final String displayName) {
        return Participant.of(id, displayName, ParticipantType.TEMPORAL_PASSENGER);
    }

    public Participant createCarDriver(final UUID id, final String displayName) {
        return Participant.of(id, displayName, ParticipantType.CAR_DRIVER);
    }

    public Car createCar(final CarFullDto carDto) {
        return Car.of(createCarId(carDto.getId()), carDto.getDriver().getId(), carDto.getSeatsCount(), 0);
    }
}
