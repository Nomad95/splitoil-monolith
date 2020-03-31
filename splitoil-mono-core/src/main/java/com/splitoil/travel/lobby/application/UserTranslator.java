package com.splitoil.travel.lobby.application;

import com.splitoil.shared.annotation.AntiCorruptionLayer;
import com.splitoil.travel.lobby.domain.model.Driver;
import com.splitoil.travel.lobby.domain.model.LobbyCreator;
import com.splitoil.user.application.UserService;
import com.splitoil.user.dto.ApplicationUserDto;
import com.splitoil.user.shared.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AntiCorruptionLayer
@AllArgsConstructor
public class UserTranslator {

    private final UserService userService;

    private final LobbyCreator lobbyCreator;

    public Driver getCurrentLoggedDriver() {
        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final ApplicationUserDto userByLogin = userService.getUserByLogin(currentUserLogin);
        return lobbyCreator.createDriver(userByLogin.getId());
    }
}
