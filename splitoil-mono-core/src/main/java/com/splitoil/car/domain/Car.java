package com.splitoil.car.domain;

import com.splitoil.car.dto.*;
import com.splitoil.shared.AbstractEntity;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Car extends AbstractEntity {

    private String brand;

    private String name;

    private Long mileage;

    @Embedded
    private Driver owner;

    private FuelTank fuelTank;

    @Builder.Default
    private Integer numberOfTravels = 0;

    @Builder.Default
    private BigDecimal manualAvgFuelConsumption = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal calculatedAvgFuelConsumption = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal calculatedAvg1kmCost = BigDecimal.ZERO;

    private BigDecimal getFuelCapacity() {
        if (Objects.isNull(fuelTank)) {
            return BigDecimal.ZERO;
        }

        return fuelTank.getCapacity();
    }

    void addFuelTank(final FuelTank fuelTank) {
        this.fuelTank = fuelTank;
    }

    void setCarsInitialMileage(final Long mileage) {
        if (this.mileage != 0L) {
            throw new IllegalStateException("Car has initial mileage already defined");
        }

        this.mileage = mileage;
    }

    void setAverageFuelConsumption(final BigDecimal avgFuelConsumption) {
        this.manualAvgFuelConsumption = avgFuelConsumption;
    }

    void addTravelInfo(final TravelEndedEvent travelEndedEvent) {
        mileage += travelEndedEvent.getTravelLength();
        numberOfTravels++;
    }

    void calculateAvgFuelConsumption(final BigDecimal totalFuelInLitres) {
        final long mileagePer100km = mileage / 100;
        calculatedAvgFuelConsumption = totalFuelInLitres.divide(new BigDecimal(mileagePer100km).setScale(2, HALF_UP), HALF_UP);
    }

    void calculateAvg1kmCost(final BigDecimal totalFuelCost) {
        calculatedAvg1kmCost = totalFuelCost.divide(new BigDecimal(mileage).setScale(2, HALF_UP), HALF_UP);
    }

    boolean isAbleToCalculateAverages() {
        return mileage > 500;
    }

    CarOutputDto toDto() {
        return CarOutputDto.builder()
            .brand(brand)
            .driver(DriverDto.of(owner.getDriverId()))
            .id(getId())
            .name(name)
            .mileage(mileage)
            .avgFuelConsumption(manualAvgFuelConsumption)
            .fuelCapacity(getFuelCapacity())
            .build();
    }

    CarFullDto toFullDto() {
        return CarFullDto.builder()
            .brand(brand)
            .driver(DriverDto.of(owner.getDriverId()))
            .id(getId())
            .name(name)
            .mileage(mileage)
            .avgFuelConsumption(calculatedAvgFuelConsumption)
            .avgCostPer1Km(calculatedAvg1kmCost)
            .fuelCapacity(getFuelCapacity())
            .numberOfTravels(numberOfTravels)
            .build();
    }

    public CarView toViewDto() {
        return CarView.builder()
            .id(getId())
            .driverId(owner.getDriverId())
            .name(name)
            .brand(brand)
            .mileage(mileage)
            .build();
    }
}
