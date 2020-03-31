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

    private UserTranslator userTranslator;

    //TODO: przez ile lobby moze byc w tym stanie?
    public LobbyOutputDto createLobby(final CreateLobbyCommand createLobbyCommand) {
        final Driver lobbyCreator = userTranslator.getCurrentLoggedDriver();
        final Lobby lobby = creator.createNewLobby(createLobbyCommand.getLobbyName(), lobbyCreator);

        lobbyRepository.save(lobby);

        return lobby.toDto();
    }

    public Result addCarToLobby(final AddCarToTravelCommand addCarToTravelCommand) {
        final CarId car = creator.createCar(addCarToTravelCommand.getCarId());
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
}
