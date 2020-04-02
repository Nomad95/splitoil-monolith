package com.splitoil.car.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class CarView {

    private UUID id;

    private String brand;

    private String name;

    private UUID driverId;

    private Long mileage;
}
