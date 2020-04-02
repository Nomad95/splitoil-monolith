package com.splitoil.car.domain

import com.splitoil.UnitTest
import com.splitoil.car.dto.AddCarDto
import com.splitoil.car.dto.DriverDto
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.Specification

@Category(UnitTest)
@Narrative("""
As an user i want to add a car to my car list and manage it""")
class CarsAddingTest extends Specification {

    static final UUID DRIVER_ID = UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')
    static final DriverDto DRIVER_DTO = DriverDto.of(DRIVER_ID)
    static final AddCarDto CAR_INPUT_DTO = AddCarDto.builder().name("A4").brand("Audi").driver(DRIVER_DTO).build()

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
            allCars.size() == 1
            allCars[0].brand == "Audi"
            allCars[0].name == "A4"
            allCars[0].driverId == DRIVER_ID
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

    //TODO:     And user is now a driver
}