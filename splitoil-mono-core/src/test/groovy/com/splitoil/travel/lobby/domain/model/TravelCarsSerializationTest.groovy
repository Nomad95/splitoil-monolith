package com.splitoil.travel.lobby.domain.model

import com.splitoil.UnitTest
import com.splitoil.infrastructure.json.JacksonAdapter
import com.splitoil.travel.lobby.domain.model.Car
import com.splitoil.travel.lobby.domain.model.CarId
import com.splitoil.travel.lobby.domain.model.TravelCars
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class TravelCarsSerializationTest extends Specification {

    public static final String EMPTY = '{"cars":{}}'
    public static final String WITH_CAR = '{"cars":{"0ea7db01-5f68-409b-8130-e96e8d96060a":{"carId":{"carId":"0ea7db01-5f68-409b-8130-e96e8d96060a"},"driverId":"0ea7db01-5f68-409b-8130-e96e8d96060a","numberOfSeats":1,"seatsOccupied":1}}}'

    def 'serialize empty'() {
        when:
            def cars = TravelCars.empty()
            def json = JacksonAdapter.getInstance().toJson(cars)

        then:
            json == EMPTY
    }

    def 'deserialize empty'() {
        when:
            def empty = TravelCars.empty()
            def cars = JacksonAdapter.getInstance().jsonDecode(EMPTY, TravelCars)

        then:
            empty == cars
    }

    def 'serialize'() {
        when:
            def cars = TravelCars.empty()
            cars.addCar(Car.of(CarId.of(UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')), UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a'), 1, 1))
            def json = JacksonAdapter.getInstance().toJson(cars)

        then:
            json == WITH_CAR
    }

    def 'deserialize'() {
        when:
            def someCars = TravelCars.empty()
            someCars.addCar(Car.of(CarId.of(UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')), UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a'), 1, 1))

            def cars = JacksonAdapter.getInstance().jsonDecode(WITH_CAR, TravelCars)

        then:
            someCars == cars
    }
}
