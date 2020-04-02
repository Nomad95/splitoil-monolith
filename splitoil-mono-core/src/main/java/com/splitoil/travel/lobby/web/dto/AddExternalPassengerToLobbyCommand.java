package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddExternalPassengerToLobbyCommand {
    @NonNull private UUID lobbyId;
    @NotBlank private String displayName;
}
