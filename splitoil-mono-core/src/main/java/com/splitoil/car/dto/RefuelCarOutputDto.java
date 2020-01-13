package com.splitoil.car.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefuelCarOutputDto {

    @NotNull
    private Long carId;

    @NotNull
    private Instant date;

    @Nullable
    private String gasStationName;

    @NotNull
    private BigDecimal cost;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String petrolType;
}
