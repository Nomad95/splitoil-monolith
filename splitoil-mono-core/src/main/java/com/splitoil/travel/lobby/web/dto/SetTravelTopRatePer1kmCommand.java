package com.splitoil.travel.lobby.web.dto;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetTravelTopRatePer1kmCommand {
    @NonNull
    private UUID lobbyId;

    @Positive
    @NonNull
    @DecimalMin("0.01")
    private BigDecimal rate;
}
