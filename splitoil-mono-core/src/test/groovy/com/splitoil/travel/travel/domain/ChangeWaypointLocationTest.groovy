package com.splitoil.travel.travel.domain

import com.splitoil.UnitTest
import com.splitoil.travel.travelflow.domain.event.WaypointLocationMoved
import com.splitoil.travel.travelflow.web.dto.MoveWaypointCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a lobby creator i want to be able to edit location of waypoints
""")
@See('resources/cucumber/defining_travel.feature')
class ChangeWaypointLocationTest extends TravelTest {

    def "Lobby creator can move travel beginning location"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()

        when: 'Driver selects waypoint to move'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def command = MoveWaypointCommand.of(travel.travelId, route.getWaypoints().get(0).getId(), SOME_LOCATION)

            travelFlowFacade.moveWaypoint(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Waypoint is moved'
            route.getWaypoints().get(0).getLocation() == SOME_LOCATION

            1 * eventPublisher.publish(_ as WaypointLocationMoved)
    }

    def "Can't change location of not existing waypoint in travel"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()

        when: 'Driver selects waypoint to move'
            def command = MoveWaypointCommand.of(travel.travelId, UUID.randomUUID(), SOME_LOCATION)
            travelFlowFacade.moveWaypoint(command)

        then:
            thrown(IllegalArgumentException)
    }

}
