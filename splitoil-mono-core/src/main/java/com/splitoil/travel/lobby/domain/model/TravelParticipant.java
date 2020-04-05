package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.AbstractEntity;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userId", "lobby"}, callSuper = false) //TODO: tak bo bedzie zmiana w AbstractEntity
public class TravelParticipant extends AbstractEntity {

    @NonNull
    private UUID userId;

    @NotBlank
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lobby lobby;

    private CarId carId;

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


