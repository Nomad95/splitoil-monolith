package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ForTravelCreationLobbyDto {
    @NonNull private List<LobbyParticipantForTravelPlanDto> participants;
}
