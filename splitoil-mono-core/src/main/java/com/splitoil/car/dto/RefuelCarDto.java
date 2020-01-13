package com.splitoil.car.dto;

import com.splitoil.gasstation.dto.GasStationIdDto;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefuelCarDto {

    @NotNull
    private Long carId;

    @NotNull
    private Instant date;

    @Nullable
    private GasStationIdDto gasStation;

    @NotNull
    private BigDecimal cost;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String petrolType;
}
