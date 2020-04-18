package com.splitoil.travel.lobby.application;

import com.splitoil.shared.annotation.AntiCorruptionLayer;
import com.splitoil.travel.lobby.domain.model.Driver;
import com.splitoil.travel.lobby.domain.model.LobbyCreator;
import com.splitoil.travel.lobby.domain.model.Participant;
import com.splitoil.user.application.UserFacade;
import com.splitoil.user.dto.ApplicationUserDto;
import com.splitoil.user.shared.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AntiCorruptionLayer
@AllArgsConstructor
public class UserTranslationService {

    private final UserFacade userFacade;

    private final LobbyCreator lobbyCreator;

    public Driver getCurrentLoggedDriver() {
        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final ApplicationUserDto userByLogin = userFacade.getUserByLogin(currentUserLogin);
        return lobbyCreator.createDriver(userByLogin.getId());
    }

    public Participant getCurrentUserAsDriver() {
        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final ApplicationUserDto userByLogin = userFacade.getUserByLogin(currentUserLogin);
        return lobbyCreator.createCarDriver(userByLogin.getId(), userByLogin.getLogin(), userByLogin.getDefaultCurrency());
    }

    public Participant getAsDriver(final UUID driverId) {
        final ApplicationUserDto userByLogin = userFacade.getUserById(driverId);
        return lobbyCreator.createCarDriver(userByLogin.getId(), userByLogin.getLogin(), userByLogin.getDefaultCurrency());
    }

    public Participant getPassenger(final UUID userId) {
        final ApplicationUserDto user = userFacade.getUserById(userId);
        return lobbyCreator.createPassenger(user.getId(), user.getLogin(), user.getDefaultCurrency());
    }
}
