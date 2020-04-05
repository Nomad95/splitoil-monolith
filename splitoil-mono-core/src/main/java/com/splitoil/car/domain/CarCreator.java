package com.splitoil.car.domain;

import com.splitoil.car.dto.*;

class CarCreator {

    Driver createDriver(final DriverDto driverDto) {
        return Driver.of(driverDto.getId());
    }

    Car createCar(final AddCarDto addCarDto) {
        return Car.builder()
            .brand(addCarDto.getBrand())
            .name(addCarDto.getName())
            .seatCount(addCarDto.getSeatsCount())
            .mileage(0L)
            .owner(createDriver(addCarDto.getDriver()))
            .build();
    }

    FuelTank createFuelTank(final FuelTankDto fuelTankDto) {
        return FuelTank.builder()
            .capacity(fuelTankDto.getCapacity())
            .petrolType(PetrolType.valueOf(fuelTankDto.getFuelType()))
            .build();
    }

    CarCost createCarCost(final AddCarCostDto costDto) {
        return CarCost.builder()
            .costDate(costDto.getDate())
            .carId(costDto.getCarId())
            .name(costDto.getName())
            .value(costDto.getValue())
            .build();
    }

    CarRefuel createCarRefuel(final RefuelCarDto refuelCarDto) {
        return CarRefuel.builder()
            .carId(refuelCarDto.getCarId())
            .costDate(refuelCarDto.getDate())
            .fuelAmountInLitres(refuelCarDto.getAmount())
            .gasStation(GasStation.from(refuelCarDto.getGasStation()))
            .petrolType(PetrolType.valueOf(refuelCarDto.getPetrolType()))
            .value(refuelCarDto.getCost())
            .build();
    }

}
