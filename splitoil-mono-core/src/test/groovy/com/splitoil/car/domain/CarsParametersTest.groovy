package com.splitoil.car.domain

import com.splitoil.UnitTest
import com.splitoil.car.dto.*
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import spock.lang.Specification

import java.time.Instant

@Category(UnitTest)
class CarsParametersTest extends Specification {

    static final UUID DRIVER_ID = UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')
    static final DriverDto DRIVER_DTO = DriverDto.of(DRIVER_ID)
    static final AddCarDto CAR_INPUT_DTO = AddCarDto.builder().name("A4").brand("Audi").driver(DRIVER_DTO).build()
    static final double LONGITUDE = -75.56
    static final double LATITUDE = 14.54
    static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), GAS_STATION_NAME)
    static final Instant NOW = Instant.now()

    private CarFacade carFacade

    def setup() {
        carFacade = new CarConfiguration().carFacade()
    }

    def "Driver should be able to add fuel tank to his car"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def fuelTankDto = FuelTankDto.builder()
                    .fuelType("BENZINE_95")
                    .capacity(new BigDecimal("65"))
                    .carId(addedCar.getId())
                    .build()

        when: "driver defines fuel tank"
            def car = carFacade.editCarsFuelTank(fuelTankDto)

        then:
            car.fuelCapacity == new BigDecimal("65")
    }

    def "Driver should be able to add initial mileage to car"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def mileageDto = AddCarMileageDto.builder()
                    .mileage(150_000)
                    .carId(addedCar.getId())
                    .build()

        when: "driver defines initial car mileage tank"
            def car = carFacade.setCarsInitialMileage(mileageDto)

        then:
            car.mileage == 150_000
    }

    def "Driver should be able to set avg fuel consumption manually"() {
        def fuelConsumption = new BigDecimal("11.5")

        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def avgFuelConsumptionDto = AddCarAverageFuelConsumptionDto.builder()
                    .avgFuelConsumption(fuelConsumption)
                    .carId(addedCar.getId())
                    .build()

        when: "driver defines avg fuel consumption"
            def car = carFacade.editCarsAverageFuelConsumptionManually(avgFuelConsumptionDto)

        then:
            car.avgFuelConsumption == fuelConsumption
    }


}
