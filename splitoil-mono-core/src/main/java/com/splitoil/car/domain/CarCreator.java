package com.splitoil.car.domain;

import com.splitoil.car.dto.*;
import com.splitoil.shared.UserCurrencyProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class CarCreator {

    private final UserCurrencyProvider currencyProvider;

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
            .currency(currencyProvider.getCurrentUserDefaultCurrency())
            .build();
    }

    CarRefuel createCarRefuel(final RefuelCarDto refuelCarDto) {
        return CarRefuel.builder()
            .carId(refuelCarDto.getCarId())
            .costDate(refuelCarDto.getDate())
            .fuelAmountInLitres(refuelCarDto.getAmount())
            .gasStation(GasStation.from(refuelCarDto.getGasStation()))
            .petrolType(PetrolType.valueOf(refuelCarDto.getPetrolType()))
            .currency(currencyProvider.getCurrentUserDefaultCurrency())
            .value(refuelCarDto.getCost())
            .build();
    }

}
