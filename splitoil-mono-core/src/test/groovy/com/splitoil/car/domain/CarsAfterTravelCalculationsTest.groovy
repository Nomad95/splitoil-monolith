package com.splitoil.car.domain

import com.splitoil.UnitTest
import com.splitoil.car.dto.AddCarDto
import com.splitoil.car.dto.DriverDto
import com.splitoil.car.dto.RefuelCarDto
import com.splitoil.car.dto.TravelEndedEvent
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import spock.lang.Specification

import java.time.Instant

@Category(UnitTest)
class CarsAfterTravelCalculationsTest extends Specification {

    static final UUID DRIVER_ID = UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')
    static final DriverDto DRIVER_DTO = DriverDto.of(DRIVER_ID)
    static final AddCarDto CAR_INPUT_DTO = AddCarDto.builder().name("A4").brand("Audi").seatsCount(5).driver(DRIVER_DTO).build()
    static final double LONGITUDE = -75.56
    static final double LATITUDE = 14.54
    static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), GAS_STATION_NAME)
    static final Instant NOW = Instant.now()

    private CarFacade carFacade

    def setup() {
        carFacade = new CarConfiguration().carFacade()
    }

    def "Received end travel event adds mileage to a car"() {
        given: 'an ended travel'
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def travelEndedEvent = TravelEndedEvent.builder()
                    .carId(addedCar.id)
                    .dateEnded(Instant.now())
                    .dateStarted(Instant.now())
                    .travelLength(600L)
                    .build()

        when: 'system processes the event'
            carFacade.handleTravelEnded(travelEndedEvent)

        then: 'car state was changed'
            def car = carFacade.getOneCar(addedCar.id)
            car.mileage == 600L
            car.numberOfTravels == 1
    }


    def "Car travelled 500km and can have his averages calculated"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def refuelCommand = RefuelCarDto.builder()
                    .carId(addedCar.getId())
                    .petrolType("BENZINE_95")
                    .amount(new BigDecimal("2500.0"))
                    .gasStation(GAS_STATION_ID_DTO)
                    .cost(new BigDecimal("1000.0"))
                    .date(NOW)
                    .build()
            def travelEndedEvent = TravelEndedEvent.builder()
                    .carId(addedCar.id)
                    .dateEnded(Instant.now())
                    .dateStarted(Instant.now())
                    .travelLength(600L)
                    .build()
            carFacade.addCarRefuel(refuelCommand)

        when: "driver adds a refuel to his car"
            carFacade.handleTravelEnded(travelEndedEvent)

        then:
            def car = carFacade.getOneCar(addedCar.id)
            car.mileage == 600L
            car.avgFuelConsumption == new BigDecimal("416.7")
            car.avgCostPer1Km == new BigDecimal("1.7")
            car.id == addedCar.id
            car.brand == "Audi"
            car.name == "A4"
    }
}
