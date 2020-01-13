package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarOutputDto {

    private Long id;

    private String brand;

    private String name;

    private DriverDto driver;

    private Long mileage;

    private BigDecimal fuelCapacity;

    private BigDecimal avgFuelConsumption;

}