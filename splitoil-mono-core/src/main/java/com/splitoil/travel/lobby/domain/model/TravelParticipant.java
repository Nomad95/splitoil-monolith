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
@EqualsAndHashCode(of = { "userId", "lobby" }, callSuper = false)
public class TravelParticipant extends AbstractEntity {//TODO: zmiana na bez aggregate

    @NonNull
    private UUID userId;

    @NotBlank
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lobby lobby;

    private CarId assignedCar;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Currency travelCurrency;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ParticipantType participantType;

    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private boolean costCharging = true;

    LobbyParticipantDto toDto() {
        return LobbyParticipantDto.builder()
            .displayName(displayName)
            .userId(userId)
            .participantType(participantType.name())
            .costChargingEnabled(costCharging)
            .travelCurrency(travelCurrency.name())
            .assignedCar(assignedCar.getCarId())
            .build();
    }

    boolean hasUserId(final @NonNull UUID userId) {
        return this.userId.equals(userId);
    }

    public void toggleCostCharging() {
        this.costCharging = !this.costCharging;
    }

    public void changeTravelCurrency(final @NonNull Currency currency) {
        travelCurrency = currency;
    }

    public void reseatTo(final CarId car) {
        assignedCar = car;
    }

    public boolean isDriver() {
        return participantType == ParticipantType.CAR_DRIVER;
    }
}


