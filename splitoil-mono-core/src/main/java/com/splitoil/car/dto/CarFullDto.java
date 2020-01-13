package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarFullDto {

    private Long id;

    private String brand;

    private String name;

    private DriverDto driver;

    private Long mileage;

    private BigDecimal fuelCapacity;

    private BigDecimal avgFuelConsumption; //per 100 km

    private Integer numberOfTravels;

    private BigDecimal avgCostPer1Km;
}
