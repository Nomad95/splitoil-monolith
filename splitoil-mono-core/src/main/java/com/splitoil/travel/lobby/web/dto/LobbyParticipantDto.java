package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LobbyParticipantDto {
    @NonNull private String displayName;
    @NonNull private UUID userId;
    @NonNull private String participantType;
}