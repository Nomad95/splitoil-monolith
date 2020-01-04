package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification
import spock.lang.Unroll


@Category(UnitTest)
class GasStationRatingsTest extends Specification {

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
            gasStation.getOverallRating() == new BigDecimal(expected).setScale(2, GasStation.ROUNDING_MODE)
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
            gasStation.getOverallRating() == new BigDecimal(3.31).setScale(2, GasStation.ROUNDING_MODE)
    }

    def "should rate gas station no rating case"() {
        expect:
            new GasStationCreator().create(new GasStationId(new GeoPoint(lon: 22.5, lat: 100.0), "Orlen")).getOverallRating() == new BigDecimal(0).setScale(2, GasStation.ROUNDING_MODE)
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
