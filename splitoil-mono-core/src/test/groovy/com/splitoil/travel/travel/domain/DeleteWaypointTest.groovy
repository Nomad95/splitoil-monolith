package com.splitoil.travel.travel.domain

import com.splitoil.UnitTest
import com.splitoil.travel.travel.domain.event.WaypointDeleted
import com.splitoil.travel.travel.web.dto.AddCheckpointCommand
import com.splitoil.travel.travel.web.dto.AddRefuelPlaceCommand
import com.splitoil.travel.travel.web.dto.DeleteWaypointCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a lobby creator i want to be able to remove waypoints in my travel plan
""")
@See('resources/cucumber/defining_travel.feature')
class DeleteWaypointTest extends TravelTest {

    def setup() {
        allPassengersAndCarsExistsInLobby()
    }

    def "Lobby creator can remove waypoint"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver deletes refuel waypoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def refuelWaypointId = route.getWaypoints().get(1).getId()
            def command = DeleteWaypointCommand.of(travel.travelId, refuelWaypointId)

            travelFlowFacade.deleteWaypoint(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Refuel is no longer in route view'
            route.getWaypoints().get(0).waypointType == "BEGINNING_PLACE"
            route.getWaypoints().get(1).waypointType == "CHECKPOINT"
            route.getWaypoints().get(2).waypointType == "DESTINATION_PLACE"

            1 * eventPublisher.publish(_ as WaypointDeleted)
    }

    def "Lobby creator can't remove waypoint that is not in the travel"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Deleting not existing travel'
            def command = DeleteWaypointCommand.of(travel.travelId, UUID.randomUUID())
            travelFlowFacade.deleteWaypoint(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can't remove beginning place waypoint"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Deleting beginning waypoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def beginningWaypointId = route.getWaypoints().get(0).getId()
            def command = DeleteWaypointCommand.of(travel.travelId, beginningWaypointId)

            travelFlowFacade.deleteWaypoint(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can't remove destination place waypoint"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Deleting beginning waypoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def destinationWaypointId = route.getWaypoints().get(3).getId()
            def command = DeleteWaypointCommand.of(travel.travelId, destinationWaypointId)

            travelFlowFacade.deleteWaypoint(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can't remove waypoint that has been traveled to (historical)"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Deleting beginning waypoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def destinationWaypointId = route.getWaypoints().get(3).getId()
            def command = DeleteWaypointCommand.of(travel.travelId, destinationWaypointId)

            travelFlowFacade.deleteWaypoint(command)

        then:
            thrown(IllegalArgumentException)
    }
}
