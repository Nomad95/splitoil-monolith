package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LobbyOutputDto {
    @NonNull private UUID lobbyId;
    @NonNull private String lobbyStatus;
    @NonNull private BigDecimal topRatePer1km;
    @NonNull private String travelCurrency;
    private List<LobbyParticipantDto> participants;
}
