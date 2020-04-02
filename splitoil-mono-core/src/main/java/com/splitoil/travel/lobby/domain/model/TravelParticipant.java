package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.AbstractEntity;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userId", "lobbyId"}, callSuper = false)
public class TravelParticipant extends AbstractEntity {

    @NonNull
    private UUID userId;

    @NonNull
    private UUID lobbyId;

    @NotBlank
    private String displayName;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Currency travelCurrency;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ParticipantType participantType;

    LobbyParticipantDto toDto() {
        return LobbyParticipantDto.builder()
            .displayName(displayName)
            .userId(userId)
            .participantType(participantType.name())
            .build();
    }
}


