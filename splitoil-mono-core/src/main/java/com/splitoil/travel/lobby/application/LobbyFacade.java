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
public class LobbyFacade {

    private final LobbyCreator creator;

    private final LobbyRepository lobbyRepository;

    private final EventPublisher eventPublisher;

    private final UserTranslationService userTranslationService;

    private final CarTranslationService carTranslationService;

    private final LobbyService lobbyService;

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
        final Participant carDriver = userTranslationService.getAsDriver(addCarToTravelCommand.getDriverId());

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

    public LobbyOutputDto addTemporalPassenger(final AddTemporalPassengerToLobbyCommand addTemporalPassengerToLobbyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(addTemporalPassengerToLobbyCommand.getLobbyId());
        final Participant passenger = creator
            .createTemporalPassenger(UUID.randomUUID(), addTemporalPassengerToLobbyCommand.getDisplayName(), lobby.getTravelCurrency().name());
        final CarId car = creator.createCarId(addTemporalPassengerToLobbyCommand.getCarId());

        lobby.addPassengerToCar(passenger, car);
        eventPublisher.publish(new ParticipantAddedToLobby(lobby.getAggregateId(), passenger.getParticipantId(), car.getCarId()));

        return lobby.toDto();
    }

    public LobbyOutputDto toggleCostCharging(final ToggleParticipantsCostChargingCommand toggleParticipantsCostChargingCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(toggleParticipantsCostChargingCommand.getLobbyId());
        final UUID participantId = toggleParticipantsCostChargingCommand.getParticipantId();

        lobby.toggleCostCharging(participantId);
        eventPublisher.publish(new ParticipantCostChargingChanged(lobby.getAggregateId(), participantId, lobby.hasCostChargingEnabled(participantId)));

        return lobby.toDto();
    }

    public LobbyOutputDto changeParticipantsCurrency(final ChangeParticipantsTravelCurrencyCommand changeParticipantsTravelCurrencyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(changeParticipantsTravelCurrencyCommand.getLobbyId());
        final Currency currency = Currency.valueOf(changeParticipantsTravelCurrencyCommand.getCurrency());
        final UUID participantId = changeParticipantsTravelCurrencyCommand.getParticipantId();

        lobby.changeParticipantTravelCurrency(participantId, currency);
        eventPublisher.publish(new ParticipantTravelCurrencyChanged(lobby.getAggregateId(), participantId, changeParticipantsTravelCurrencyCommand.getCurrency()));

        return lobby.toDto();
    }

    public LobbyOutputDto assignToCar(final AssignToCarCommand assignToCarCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(assignToCarCommand.getLobbyId());

        final CarId car = creator.createCarId(assignToCarCommand.getCarId());
        final UUID participantId = assignToCarCommand.getParticipantId();
        lobby.assignParticipantToCar(car, participantId);
        eventPublisher.publish(new ParticipantReseated(lobby.getAggregateId(), participantId, assignToCarCommand.getCarId()));

        return lobby.toDto();
    }

    public LobbyOutputDto removeFromLobby(final RemoveParticipantFromLobbyCommand removeParticipantFromLobbyCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(removeParticipantFromLobbyCommand.getLobbyId());

        final UUID participantId = removeParticipantFromLobbyCommand.getParticipantId();
        lobby.removeParticipant(participantId);
        eventPublisher.publish(new ParticipantRemovedFromLobby(lobby.getAggregateId(), participantId));

        return lobby.toDto();
    }

    public void startDefiningTravelPlan(final StartDefiningTravelPlanCommand startDefiningTravelPlanCommand) {
        final Lobby lobby = lobbyRepository.getByAggregateId(startDefiningTravelPlanCommand.getLobbyId());

        final TravelCreationRequested createTravelCommand = lobbyService.startDefiningTravelPlan(lobby);

        eventPublisher.publish(createTravelCommand);
    }

    public void assignTravelToLobby(final UUID travelId, final UUID lobbyId) {
        final Lobby lobby = lobbyRepository.getByAggregateId(lobbyId);
        final TravelId id = creator.createTravelId(travelId);

        lobby.assignTravel(id);
    }
}
