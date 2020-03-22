package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import com.splitoil.gasstation.dto.AddToObservableDto
import com.splitoil.gasstation.dto.DriverDto
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.assertj.core.api.Assertions
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.Specification

@Category(UnitTest)
@Narrative("""
As a driver i want to add selected gas station to observed by me
""")
class GasStationsAddToObservedTest extends Specification {

    //@PendingFeature
    //@IgnoreIf
    //@Requires
    //@Retry
    //@Stepwise
    //@Title("This is easy to read")
    //@Narrative("""
    //As a user
    //I want foo
    //So that bar
    //""")
    //@See("http://spockframework.org/spec")
    //@Issue("http://my.issues.org/FOO-1")

    static final double LONGITUDE = -75.56
    static final double LATITUDE = 14.54
    static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    static final long DRIVER_ID = 1L
    static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), GAS_STATION_NAME)

    private Driver driver
    private GasStationId gasStation
    private GasStationsFacade gasStationsFacade

    def setup() {
        gasStationsFacade = new GasStationConfiguration().gasStationsFacade()

        given:
            gasStation = new GasStationId(new GeoPoint(lat: LATITUDE, lon: LONGITUDE), GAS_STATION_NAME)

        and: "a driver"
            driver = new Driver(driverId: DRIVER_ID)
    }

    def "should add gas station to observed"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def driverDto = DriverDto.of(DRIVER_ID)
            def command = new AddToObservableDto(gasStationIdDto, driverDto)

        when: "driver observes a gas station"
            def dto = gasStationsFacade.addToObservables(command)

        then:
            dto != null
            dto.name == GAS_STATION_NAME
            dto.location.lat == LATITUDE
            dto.location.lon == LONGITUDE
    }

    def "should get observed gas stations"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def driverDto = DriverDto.of(DRIVER_ID)
            def command = new AddToObservableDto(gasStationIdDto, driverDto)

        when: "driver observes a gas station"
            gasStationsFacade.addToObservables(command)

        then: "driver sees gas station in observables"
            def stations = gasStationsFacade.getObservedGasStations(DRIVER_ID)
            Assertions.assertThat(stations).contains(gasStationIdDto)
    }


}
