package com.splitoil.travel.lobby.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.domain.event.*;
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

    private UserTranslationService userTranslationService;

    private CarTranslationService carTranslationService;

    //TODO: przez ile lobby moze byc w tym stanie?
    public LobbyOutputDto createLobby(final CreateLobbyCommand createLobbyCommand) {
        final Driver lobbyCreator = userTranslationService.getCurrentLoggedDriver();
        final Lobby lobby = creator.createNewLobby(createLobbyCommand.getLobbyName(), lobbyCreator);

        lobbyRepository.save(lobby);

        return lobby.toDto();
    }

    public LobbyOutputDto addCarToLobby(final AddCarToTravelCommand addCarToTravelCommand) {
        final Car car = carTranslationService.getCar(addCarToTravelCommand.getCarId());
        final Lobby lobby = lobbyRepository.getByAggregateId(addCarToTravelCommand.getLobbyId());
        final Participant carDriver = userTranslationService.getCurrentUserAsDriver();

        lobby.addCar(car);
        lobby.addPassengerToCar(carDriver, car.getCarId());

        eventPublisher.publish(new CarAddedToLobby(lobby.getAggregateId(), car.getCarId().getCarId()));
        eventPublisher.publish(new ParticipantAddedToLobby(lobby.getAggregateId(), carDriver.getParticipantId(), car.getCarId().getCarId()));
        eventPublisher.publish(new LobbyCreated(lobby.getAggregateId()));

        return lobby.toDto();
    }

    public LobbyOutputDto getLobby(final UUID lobbyId) {
        return lobbyRepository.getByAggregateId(lobbyId).toDto();
    }

    public LobbyOutputDto setTravelTopRatePer1km(final SetTravelTopRatePer1kmCommand setTravelTopRatePer1kmCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(setTravelTopRatePer1kmCommand.getLobbyId());

        lobby.setTravelTopRatePer1km(setTravelTopRatePer1kmCommand.getRate());
        eventPublisher.publish(new TravelTopRateSet(lobby.getAggregateId(), setTravelTopRatePer1kmCommand.getRate()));

        return lobby.toDto();
    }

    public LobbyOutputDto changeTravelDefaultCurrency(final ChangeTravelDefaultCurrencyCommand changeTravelDefaultCurrencyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(changeTravelDefaultCurrencyCommand.getLobbyId());
        final Currency currency = Currency.valueOf(changeTravelDefaultCurrencyCommand.getCurrency());

        lobby.changeDefaultCurrency(currency);
        eventPublisher.publish(new TravelCurrencyChanged(lobby.getAggregateId(), currency.toString()));

        return lobby.toDto();
    }

    public LobbyOutputDto addPassenger(final AddPassengerToLobbyCommand addPassengerToLobbyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(addPassengerToLobbyCommand.getLobbyId());
        final Participant passenger = userTranslationService.getPassenger(addPassengerToLobbyCommand.getUserId());
        final CarId car = creator.createCarId(addPassengerToLobbyCommand.getCarId());

        lobby.addPassengerToCar(passenger, car);
        eventPublisher.publish(new ParticipantAddedToLobby(lobby.getAggregateId(), passenger.getParticipantId(), car.getCarId()));

        return lobby.toDto();
    }

    public LobbyOutputDto addExternalPassenger(final AddExternalPassengerToLobbyCommand addExternalPassengerToLobbyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(addExternalPassengerToLobbyCommand.getLobbyId());
        final Participant passenger = creator.createTemporalPassenger(UUID.randomUUID(), addExternalPassengerToLobbyCommand.getDisplayName());
        final CarId car = creator.createCarId(addExternalPassengerToLobbyCommand.getCarId());

        lobby.addPassengerToCar(passenger, car);
        eventPublisher.publish(new ParticipantAddedToLobby(lobby.getAggregateId(), passenger.getParticipantId(), car.getCarId()));

        return lobby.toDto();
    }
}
