package com.splitoil.gasstation.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AcceptPetrolPriceDto {

    private UUID priceUuid;

}
