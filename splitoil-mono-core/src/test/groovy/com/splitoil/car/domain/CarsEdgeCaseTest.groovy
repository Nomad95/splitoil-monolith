package com.splitoil.car.domain

import com.splitoil.UnitTest
import com.splitoil.car.dto.*
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.time.Instant

@Category(UnitTest)
class CarsEdgeCaseTest extends Specification {

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

    def "Driver adds a car into the system"() {
        given: 'a car with set mileage manually once'
            def newCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def dto1 = AddCarMileageDto.builder()
                    .mileage(150_000)
                    .carId(newCar.getId())
                    .build()
            def dto2 = AddCarMileageDto.builder()
                    .mileage(20_000)
                    .carId(newCar.getId())
                    .build()
            carFacade.setCarsInitialMileage(dto1)

        when: 'i set mileage again'
            carFacade.setCarsInitialMileage(dto2)

        then: 'exception is thrown'
            thrown(IllegalStateException)
    }

    def "Driver should not have more than 20 cars"() {
        given: 'driver with 20 cars'
            for (int i = 0; i < 21; i++) {
                carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            }

        when: 'driver adds more'
            carFacade.addNewCarToCollection(CAR_INPUT_DTO)

        then: 'exception is thrown'
            thrown(IllegalStateException)
    }

    def "Should throw when car is not found"() {
        given: 'car cost with non existing car id'
            def dto = AddCarCostDto.builder()
                    .carId(UUID.randomUUID())
                    .value(new BigDecimal("13.40"))
                    .name("Engine oil 2L")
                    .date(Instant.now())
                    .build()

        when: 'someone adds cost'
            carFacade.addCarCost(dto)

        then: 'exception is thrown'
            thrown(EntityNotFoundException)
    }

    def "Fuel capacity should not return null"() {
        when: "I add my first car"
            def newCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)

        then:
            newCar.fuelCapacity != null
    }

    def "Should throw when adding refuel with non existing car"() {
        given: "chosen car"
            def refuelCommand = RefuelCarDto.builder()
                    .carId(UUID.randomUUID())
                    .petrolType("BENZINE_95")
                    .amount(new BigDecimal("25.0"))
                    .gasStation(GAS_STATION_ID_DTO)
                    .cost(new BigDecimal("100.0"))
                    .date(NOW)
                    .build()

        when: "driver adds a refuel to his car"
            carFacade.addCarRefuel(refuelCommand)

        then:
            thrown(EntityNotFoundException)
    }

}
