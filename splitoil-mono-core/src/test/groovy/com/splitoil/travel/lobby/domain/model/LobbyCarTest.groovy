package com.splitoil.travel.lobby.domain.model

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.domain.model.Car
import org.junit.experimental.categories.Category

@Category(UnitTest)
class LobbyCarTest extends LobbyModelTest {

    def "Can't disoccupy seat when car is empty"() {
        given:
            def car = Car.withDriver(CarId.of(CAR_ID), DRIVER_ID, 1)

        when:
            car = car.disoccupySeat()
            car.disoccupySeat()

        then:
            thrown(IllegalStateException)
    }

    def "Can't occupy seat when car is full"() {
        given:
            def car = Car.withDriver(CarId.of(CAR_ID), DRIVER_ID, 1)

        when:
            car.occupySeat()

        then:
            thrown(IllegalStateException)
    }

    def "Can't create car with 0 or less seats"() {
        when:
            Car.withDriver(CarId.of(CAR_ID), DRIVER_ID, 0)

        then:
            thrown(IllegalArgumentException)
    }

    def "Create car"() {
        when:
            def car = Car.withDriver(CarId.of(CAR_ID), DRIVER_ID, 5)

        then:
            noExceptionThrown()
            car.seatsOccupied == 1
            car.carId == CarId.of(CAR_ID)
            car.driverId == DRIVER_ID
            car.seatsOccupied == 1
            car.numberOfSeats == 5
    }

}
