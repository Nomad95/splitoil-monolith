package com.splitoil.travel.lobby.web.dto;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateLobbyCommand {
    @NonNull private String name;
    @NonNull private Long driverId;
}
