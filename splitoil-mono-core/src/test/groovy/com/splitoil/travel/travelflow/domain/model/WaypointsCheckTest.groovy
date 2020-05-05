package com.splitoil.travel.travelflow.domain.model

import com.splitoil.UnitTest
import com.splitoil.travel.travelflow.domain.TravelTest
import org.junit.experimental.categories.Category
import spock.lang.Unroll

@Category(UnitTest)
class WaypointsCheckTest extends TravelTest {

    public static final int NUMBER_OF_NEW_WAYPOINTS = 3
    public static final int NUMBER_OF_INITIAL_WAYPOINTS = 2

    private static final GeoPoint LOCATION = GeoPoint.of(0, 0);
    private static final GeoPoint ANOTHER_LOCATION = GeoPoint.of(50, 150);

    def "Can't add two beginning waypoints"() {
        given: 'Route with beginning waypoint'
            def travelPlan = new Route()
            travelPlan.addWaypoint(Waypoint.beginningPlace(LOCATION))

        when: 'Add second beginning waypoint'
            travelPlan.addWaypoint(Waypoint.beginningPlace(LOCATION))

        then:
            thrown(IllegalArgumentException)
    }

    def "Can't add two destination waypoints"() {
        given: 'Route with destination waypoint'
            def travelPlan = new Route()
            travelPlan.addWaypoint(Waypoint.destinationPlace(LOCATION))

        when: 'Add second destination waypoint'
            travelPlan.addWaypoint(Waypoint.destinationPlace(LOCATION))

        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "Can add any amount of given waypoints"(final Waypoint[] waypoints) {
        given:
            def travelPlan = new Route()
            travelPlan.addWaypoint(Waypoint.beginningPlace(LOCATION))
            travelPlan.addWaypoint(Waypoint.destinationPlace(LOCATION))

        when: 'Add three waypoints'
            for (Waypoint waypoint : waypoints)
                travelPlan.addWaypoint(waypoint)

        then: 'three'
            travelPlan.waypoints().size() == NUMBER_OF_NEW_WAYPOINTS + NUMBER_OF_INITIAL_WAYPOINTS

        where:
            waypoints                                                                                                                               | _
            [Waypoint.checkpoint(LOCATION), Waypoint.checkpoint(LOCATION), Waypoint.checkpoint(LOCATION)]                                           | _
            [Waypoint.participantBoardingPlace(LOCATION), Waypoint.participantBoardingPlace(LOCATION), Waypoint.participantBoardingPlace(LOCATION)] | _
            [Waypoint.passengerExitPlace(LOCATION), Waypoint.passengerExitPlace(LOCATION), Waypoint.passengerExitPlace(LOCATION)]                   | _
            [Waypoint.refuelPlace(LOCATION), Waypoint.refuelPlace(LOCATION), Waypoint.refuelPlace(LOCATION)]                                        | _
            [Waypoint.reseatPlace(LOCATION), Waypoint.reseatPlace(LOCATION), Waypoint.reseatPlace(LOCATION)]                                        | _
            [Waypoint.stopPlace(LOCATION), Waypoint.stopPlace(LOCATION), Waypoint.stopPlace(LOCATION)]                                              | _
    }

    @Unroll
    def "Can't add any else type waypoint before adding beginning and destination place"(final Waypoint waypoint) {
        given: 'Blank route'
            def travelPlan = new Route()

        when: 'Add waypoint'
            travelPlan.addWaypoint(waypoint)

        then:
            thrown(IllegalArgumentException)

        where:
            waypoint                                    | _
            Waypoint.checkpoint(LOCATION)               | _
            Waypoint.participantBoardingPlace(LOCATION) | _
            Waypoint.passengerExitPlace(LOCATION)       | _
            Waypoint.refuelPlace(LOCATION)              | _
            Waypoint.reseatPlace(LOCATION)              | _
            Waypoint.stopPlace(LOCATION)                | _
    }

    def "Beginning is always first waypoint and destination last"() {
        given: 'Blank route'
            def travelPlan = new Route()

            def beginning = Waypoint.beginningPlace(LOCATION)
            def destination = Waypoint.destinationPlace(LOCATION)

            travelPlan.addWaypoint(beginning)
            travelPlan.addWaypoint(destination)

        when: 'Add waypoints'
            travelPlan.addWaypoint(Waypoint.checkpoint(LOCATION))
            travelPlan.addWaypoint(Waypoint.participantBoardingPlace(LOCATION))
            travelPlan.addWaypoint(Waypoint.passengerExitPlace(LOCATION))
            travelPlan.addWaypoint(Waypoint.refuelPlace(LOCATION))
            travelPlan.addWaypoint(Waypoint.reseatPlace(LOCATION))
            travelPlan.addWaypoint(Waypoint.stopPlace(LOCATION))

        then:
            travelPlan.waypoints().get(0) == beginning
            travelPlan.waypoints().get(travelPlan.waypoints().size() - 1) == destination
    }

    def "Can't change location of historical waypoint"() {
        given: 'Route with historical beginning place'
            def travelPlan = new Route()

            def beginning = Waypoint.beginningPlace(LOCATION)
            beginning.historical = true
            def destination = Waypoint.destinationPlace(LOCATION)

            travelPlan.addWaypoint(beginning)
            travelPlan.addWaypoint(destination)

        when: 'Moving waypoint to another place'
            travelPlan.changeLocation(beginning.id, ANOTHER_LOCATION)

        then:
            thrown(IllegalArgumentException)
    }

}
