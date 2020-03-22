package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import com.splitoil.gasstation.dto.AddRatingDto
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Unroll


@Category(UnitTest)
@Narrative("""
As a driver i want to rate gas station that is on the map
""")
class GasStationRatingsTest extends Specification {

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

    def "driver should rate gas station"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 5)

        when: "gas station has a new rating added"
            gasStationsFacade.rateGasStation(addRatingCommand)

        then: "have changed its rate value when asked"
            gasStationsFacade.getRating(gasStationIdDto) == new BigDecimal(5)
    }

    def "rating gas station returns rating immediately"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 5)

        when: "gas station has a new rating added"
            def rating = gasStationsFacade.rateGasStation(addRatingCommand)

        then: "have changed its rate value"
            rating == new BigDecimal(5)
    }

    def "driver should rate existing gas station"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 5)
            gasStationsFacade.rateGasStation(addRatingCommand)

        when: "gas station has a second rating added"
            def rating = gasStationsFacade.rateGasStation(addRatingCommand)

        then: "have changed its rate value"
            rating == new BigDecimal(5)
    }

    def "adding second rate should create average of rates"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 5)
            def addRatingCommand2 = new AddRatingDto(gasStationIdDto, 2)

        when: "gas station has a new rating added"
            gasStationsFacade.rateGasStation(addRatingCommand)
            gasStationsFacade.rateGasStation(addRatingCommand2)

        then: "have changed its rate value"
            gasStationsFacade.getRating(gasStationIdDto) == new BigDecimal("3.5")
    }

    @Unroll
    def "should rate gas station"(int r1, int r2, int r3, int r4, String expected) {
        given:
            def gasStation = new GasStationCreator().create(new GasStationId(new GeoPoint(lon: 22.5, lat: 100.0), "Orlen"))
        when:
            gasStation.addRating(Rating.of(r1))
            gasStation.addRating(Rating.of(r2))
            gasStation.addRating(Rating.of(r3))
            gasStation.addRating(Rating.of(r4))
        then:
            gasStation.getOverallRating() == new BigDecimal(expected).setScale(2, GasStation.RATING_ROUNDING)
        where:
            r1 | r2 | r3 | r4 | expected
            5  | 5  | 4  | 4  | 4.5
            1  | 1  | 1  | 4  | 1.75
            1  | 4  | 4  | 4  | 3.25
            1  | 3  | 3  | 4  | 2.75
    }

    def "should rate gas station lot o ratings case"() {
        given:
            def gasStation = new GasStationCreator().create(new GasStationId(new GeoPoint(lon: 22.5, lat: 100.0), "Orlen"))
        when:
            gasStation.addRating(Rating.of(5))
            gasStation.addRating(Rating.of(5))
            gasStation.addRating(Rating.of(4))
            gasStation.addRating(Rating.of(3))
            gasStation.addRating(Rating.of(2))
            gasStation.addRating(Rating.of(2))
            gasStation.addRating(Rating.of(2))
            gasStation.addRating(Rating.of(1))
            gasStation.addRating(Rating.of(1))
            gasStation.addRating(Rating.of(4))
            gasStation.addRating(Rating.of(4))
            gasStation.addRating(Rating.of(5))
            gasStation.addRating(Rating.of(5))
        then:
            gasStation.getOverallRating() == new BigDecimal(3.31).setScale(2, GasStation.RATING_ROUNDING)
    }

    def "should rate gas station no rating case"() {
        expect:
            new GasStationCreator().create(new GasStationId(new GeoPoint(lon: 22.5, lat: 100.0), "Orlen")).getOverallRating() == new BigDecimal(0).setScale(2, GasStation.RATING_ROUNDING)
    }

    @Unroll
    def "should not add out of scale rating"(int r1, Class expected) {
        given:
            def gasStation = new GasStationCreator().create(new GasStationId(new GeoPoint(lon: 22.5, lat: 100.0), "Orlen"))
        when:
            gasStation.addRating(Rating.of(r1))
        then:
            thrown(expected)
        where:
            r1   | expected
            0    | RatingOutOfScopeException
            6    | RatingOutOfScopeException
            (-1) | RatingOutOfScopeException
    }

}
