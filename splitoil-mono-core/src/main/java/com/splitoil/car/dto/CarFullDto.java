package com.splitoil.car.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarFullDto {

    private UUID id;

    private String brand;

    private String name;

    private DriverDto driver;

    private Long mileage;

    private BigDecimal fuelCapacity;

    private BigDecimal avgFuelConsumption; //per 100 km

    private Integer numberOfTravels;

    private BigDecimal avgCostPer1Km;

    private int seatsCount;

}
