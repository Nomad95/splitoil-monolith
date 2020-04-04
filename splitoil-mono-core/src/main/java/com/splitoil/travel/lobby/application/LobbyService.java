package com.splitoil.travel.lobby.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.dto.Result;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.domain.event.LobbyCreated;
import com.splitoil.travel.lobby.domain.model.*;
import com.splitoil.travel.lobby.web.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@ApplicationService
@AllArgsConstructor
public class LobbyService {

    private LobbyCreator creator;

    private LobbyRepository lobbyRepository;

    private EventPublisher eventPublisher;

    private UserService userService;

    private CarService carService;

    //TODO: przez ile lobby moze byc w tym stanie?
    //TODO: pousuwaj te id z drivera na UUID zamien
    public LobbyOutputDto createLobby(final CreateLobbyCommand createLobbyCommand) {
        final Driver lobbyCreator = userService.getCurrentLoggedDriver();
        final Lobby lobby = creator.createNewLobby(createLobbyCommand.getLobbyName(), lobbyCreator);

        lobbyRepository.save(lobby);

        return lobby.toDto();
    }

    public Result addCarToLobby(final AddCarToTravelCommand addCarToTravelCommand) {
        final Car car = carService.getCar(addCarToTravelCommand.getCarId());
        final Lobby lobby = lobbyRepository.getByAggregateId(addCarToTravelCommand.getLobbyId());

        lobby.addCar(car);
        eventPublisher.publish(new LobbyCreated(lobby.getAggregateId()));

        return Result.Success;
    }

    public LobbyOutputDto getLobby(final UUID lobbyId) {
        return lobbyRepository.getByAggregateId(lobbyId).toDto();
    }

    public LobbyOutputDto setTravelTopRatePer1km(final SetTravelTopRatePer1kmCommand setTravelTopRatePer1kmCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(setTravelTopRatePer1kmCommand.getLobbyId());

        lobby.setTravelTopRatePer1km(setTravelTopRatePer1kmCommand.getRate());

        return lobby.toDto();
    }

    public LobbyOutputDto changeTravelDefaultCurrency(final ChangeTravelDefaultCurrencyCommand changeTravelDefaultCurrencyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(changeTravelDefaultCurrencyCommand.getLobbyId());
        final Currency currency = Currency.valueOf(changeTravelDefaultCurrencyCommand.getCurrency());

        lobby.changeDefaultCurrency(currency);

        return lobby.toDto();
    }

    public LobbyOutputDto addPassenger(final AddPassengerToLobbyCommand addPassengerToLobbyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(addPassengerToLobbyCommand.getLobbyId());
        final Participant passenger = userService.getPassenger(addPassengerToLobbyCommand.getUserId());
        final CarId car = creator.createCarId(addPassengerToLobbyCommand.getCarId());

        lobby.addPassengerToCar(passenger, car);

        return lobby.toDto();
    }

    public LobbyOutputDto addExternalPassenger(final AddExternalPassengerToLobbyCommand addExternalPassengerToLobbyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(addExternalPassengerToLobbyCommand.getLobbyId());
        final Participant passenger = creator.createAdHocPassenger(UUID.randomUUID(), addExternalPassengerToLobbyCommand.getDisplayName());
        final CarId car = creator.createCarId(addExternalPassengerToLobbyCommand.getCarId());

        lobby.addPassengerToCar(passenger, car);

        return lobby.toDto();
    }
}
