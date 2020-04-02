package com.splitoil.car.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddCarMileageDto {

    private UUID carId;

    private Long mileage;

}
