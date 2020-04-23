package com.splitoil.travel.lobby.domain.model;

import com.splitoil.car.dto.CarFullDto;
import com.splitoil.shared.UserCurrencyProvider;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.ForTravelCreationLobbyDto;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantDto;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Participant createPassenger(final UUID id, final String displayName, final String travelCurrency) {
        return Participant.of(id, displayName, ParticipantType.PASSENGER, Currency.valueOf(travelCurrency));
    }

    public Participant createTemporalPassenger(final UUID id, final String displayName, final String travelCurrency) {
        return Participant.of(id, displayName, ParticipantType.TEMPORAL_PASSENGER, Currency.valueOf(travelCurrency));
    }

    public Participant createCarDriver(final UUID id, final String displayName, final String travelCurrency) {
        return Participant.of(id, displayName, ParticipantType.CAR_DRIVER, Currency.valueOf(travelCurrency));
    }

    public Car createCar(final CarFullDto carDto) {
        return Car.of(createCarId(carDto.getId()), carDto.getDriver().getId(), carDto.getSeatsCount(), 0);
    }

    TravelCreationRequested createRequestForTravelCreation(final Lobby lobby) {
        final List<LobbyParticipantForTravelPlanDto> participants = lobby.getParticipants().stream()
            .map(LobbyParticipantDto::toForTravelDto).collect(Collectors.toUnmodifiableList());

        final ForTravelCreationLobbyDto data = ForTravelCreationLobbyDto.builder()
            .participants(participants)
            .build();

        return new TravelCreationRequested(lobby.getAggregateId(), data);
    }
}
