package com.splitoil.car.domain

import com.splitoil.UnitTest
import com.splitoil.car.dto.*
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.Instant

@Category(UnitTest)
class CarsTest extends Specification {

    static final DriverDto DRIVER_DTO = DriverDto.of(1L)
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
        when: "I add my first car"
            def newCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)

        then:
            newCar.name == "A4"
            newCar.brand == "Audi"
            newCar.driver == DRIVER_DTO
    }

    def "Driver adds a car into the system and can retrieve it"() {
        when: "I add my first car"
            carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def allCars = carFacade.getAllCars(DRIVER_DTO.getId())

        then: "car is added to my car storage"
            def expected = CarView.builder().brand("Audi").name("A4").driverId(1L).id(1L).build()
            allCars.size() == 1
            allCars.contains(expected)
    }

    def "Driver should be able to delete his car"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)

        when: "driver deletes car"
            carFacade.deleteCar(addedCar.getId())
            def allCars = carFacade.getAllCars(DRIVER_DTO.getId())

        then:
            allCars.size() == 0
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

    def "Driver adds cost related to his car"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def addCarCostDto = AddCarCostDto.builder()
                    .carId(addedCar.getId())
                    .value(new BigDecimal("125.53"))
                    .name("Engine oil 2L")
                    .build()

        when: "driver adds a cost related to his car"
            carFacade.addCarCost(addCarCostDto)

        then:
            carFacade.getTotalCarCostsSum(addedCar.id) == new BigDecimal("125.53")
    }

    def "Car costs should be summed up when has multiple costs"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def addCarCostDto1 = AddCarCostDto.builder()
                    .carId(addedCar.getId())
                    .value(new BigDecimal("13.40"))
                    .name("Engine oil 2L")
                    .date(Instant.now())
                    .build()
            def addCarCostDto2 = AddCarCostDto.builder()
                    .carId(addedCar.getId())
                    .value(new BigDecimal("12.60"))
                    .name("Fuel")
                    .date(Instant.now())
                    .build()

        when: "driver adds a cost related to his car"
            carFacade.addCarCost(addCarCostDto1)
            carFacade.addCarCost(addCarCostDto2)

        then:
            carFacade.getTotalCarCostsSum(addedCar.id) == new BigDecimal("26.0")
    }

    def "Driver can add refuel manually"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def refuelCommand = RefuelCarDto.builder()
                    .carId(addedCar.getId())
                    .petrolType("BENZINE_95")
                    .amount(new BigDecimal("25.0"))
                    .gasStation(GAS_STATION_ID_DTO)
                    .cost(new BigDecimal("100.0"))
                    .date(NOW)
                    .build()

        when: "driver adds a refuel to his car"
            carFacade.addCarRefuel(refuelCommand)

        then:
            def expectedView = RefuelCarOutputDto.builder()
                    .carId(addedCar.getId())
                    .petrolType("BENZINE_95")
                    .amount(new BigDecimal("25.0"))
                    .gasStationName(GAS_STATION_ID_DTO.getName())
                    .cost(new BigDecimal("100.0"))
                    .date(NOW)
                    .build()

            def refuels = carFacade.getRefuels(addedCar.id, PageRequest.of(0, 20)).getContent()
            refuels.size() == 1
            refuels.contains(expectedView)
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
