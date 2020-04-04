package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddPassengerToLobbyCommand {
    @NonNull private UUID lobbyId;
    @NonNull private UUID userId;
    @NonNull private UUID carId;
}
