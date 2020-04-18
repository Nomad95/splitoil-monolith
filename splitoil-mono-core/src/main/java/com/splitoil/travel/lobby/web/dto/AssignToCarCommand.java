package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssignToCarCommand {
    @NonNull private UUID lobbyId;
    @NonNull private UUID participantId;
    @NonNull private UUID carId;
}
