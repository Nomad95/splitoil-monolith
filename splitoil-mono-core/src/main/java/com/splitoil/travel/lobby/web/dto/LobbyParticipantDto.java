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
    @NonNull private UUID assignedCar;
    @NonNull private String participantType;
    private boolean costChargingEnabled;
    @NonNull private String travelCurrency;

    public LobbyParticipantForTravelPlanDto toForTravelDto() {
        return LobbyParticipantForTravelPlanDto.builder()
            .assignedCar(assignedCar)
            .userId(userId)
            .build();
    }
}
