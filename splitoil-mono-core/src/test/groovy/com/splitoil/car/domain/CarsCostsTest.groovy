package com.splitoil.car.domain

import com.splitoil.UnitTest
import com.splitoil.car.dto.*
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import org.springframework.data.domain.PageRequest
import spock.lang.Narrative
import spock.lang.Specification

import java.time.Instant

@Category(UnitTest)
@Narrative("""
As a driver i want to""")
class CarsCostsTest extends Specification {

    static final UUID DRIVER_ID = UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')
    static final DriverDto DRIVER_DTO = DriverDto.of(DRIVER_ID)
    static final AddCarDto CAR_INPUT_DTO = AddCarDto.builder().name("A4").brand("Audi").seatsCount(5).driver(DRIVER_DTO).build()
    static final double LONGITUDE = -75.56
    static final double LATITUDE = 14.54
    static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), GAS_STATION_NAME)
    static final Instant NOW = Instant.now()
    public static final String PLN = "PLN"

    private CarFacade carFacade

    def setup() {
        carFacade = new CarConfiguration().carFacade()
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

    def "Car cost is in drivers default currency"() {
        given: "chosen car"
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def addCarCostDto = AddCarCostDto.builder()
                    .carId(addedCar.getId())
                    .value(new BigDecimal("125.53"))
                    .name("Engine oil 2L")
                    .build()

        when: "driver adds a cost related to his car"
            def details = carFacade.addCarCost(addCarCostDto)

        then:
            details.currency == PLN
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
                    .currency(PLN)
                    .date(NOW)
                    .build()

            def refuels = carFacade.getRefuels(addedCar.id, PageRequest.of(0, 20)).getContent()
            refuels.size() == 1
            refuels.contains(expectedView)
    }

    def "Currency sets as driver default when driver adds refuel manually"() {
        given: 'chosen car'
            def addedCar = carFacade.addNewCarToCollection(CAR_INPUT_DTO)
            def refuelCommand = RefuelCarDto.builder()
                    .carId(addedCar.getId())
                    .petrolType("BENZINE_95")
                    .amount(new BigDecimal("25.0"))
                    .gasStation(GAS_STATION_ID_DTO)
                    .cost(new BigDecimal("100.0"))
                    .date(NOW)
                    .build()

        when: 'driver adds a refuel to his car'
            carFacade.addCarRefuel(refuelCommand)

        then: 'Currency is set as default'
            def refuels = carFacade.getRefuels(addedCar.id, PageRequest.of(0, 20)).getContent()
            refuels.size() == 1
            refuels[0].currency == PLN
    }

}
