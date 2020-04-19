package com.splitoil.travel.lobby.domain.model

import com.splitoil.UnitTest
import org.junit.experimental.categories.Category

@Category(UnitTest)
class LobbyTravelCarTest extends LobbyModelTest {

    static final CarId CAR = CarId.of(CAR_ID)

    def "Cant occupy seat of car that is not in lobby"() {
        given:
            def travelCars = TravelCars.empty()

        when:
            travelCars.occupySeatOfCar(CAR)

        then:
            thrown(IllegalArgumentException)
    }

    def "Cant disoccupy seat of car that is not in lobby"() {
        given:
            def travelCars = TravelCars.empty()

        when:
            travelCars.disoccupySeatOfCar(CAR)

        then:
            thrown(IllegalArgumentException)
    }

    def "Cant get car that is not in lobby"() {
        given:
            def travelCars = TravelCars.empty()

        when:
            travelCars.getCar(CAR)

        then:
            thrown(IllegalArgumentException)
    }

    def "Cant remove car that is not in lobby"() {
        given:
            def travelCars = TravelCars.empty()

        when:
            travelCars.removeCar(CAR)

        then:
            thrown(IllegalArgumentException)
    }
}
