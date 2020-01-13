package com.splitoil.car.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class CarView {

    private Long id;

    private String brand;

    private String name;

    private Long driverId;

    private Long mileage;
}
