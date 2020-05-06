package com.splitoil.travel.travelflow.domain

import com.splitoil.UnitTest
import com.splitoil.travel.travelflow.web.dto.AddCheckpointCommand
import com.splitoil.travel.travelflow.web.dto.AddRefuelPlaceCommand
import com.splitoil.travel.travelflow.web.dto.AddStopPlaceCommand
import com.splitoil.travel.travelflow.web.dto.ChangeOrderWaypointCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a lobby creator i want to be able to arrange the order of waypoints
""")
@See('resources/cucumber/defining_travel.feature')
class ChangeWaypointOrderTest extends TravelTest {

    def "Lobby creator can move travel beginning location"() {
        setup: 'New travel - checkpoint is after refuel place'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges refuel after checkpoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def refuelWaypointId = route.getWaypoints().get(1).getId()
            def checkpointWaypointId = route.getWaypoints().get(2).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, refuelWaypointId, checkpointWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Refuel is after checkpoint'
            route.getWaypoints().get(0).waypointType == "BEGINNING_PLACE"
            route.getWaypoints().get(1).waypointType == "CHECKPOINT"
            route.getWaypoints().get(2).waypointType == "REFUEL_PLACE"
            route.getWaypoints().get(3).waypointType == "DESTINATION_PLACE"
    }

    def "Reorder after beginning"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addStopPlace(new AddStopPlaceCommand(travel.travelId, SOME_LOCATION))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges checkpoint after beginning'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def checkpointWaypointId = route.getWaypoints().get(3).getId()
            def beginningWaypointId = route.getWaypoints().get(0).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, checkpointWaypointId, beginningWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Checkpoint is after beginning'
            route.getWaypoints().get(0).waypointType == "BEGINNING_PLACE"
            route.getWaypoints().get(1).waypointType == "CHECKPOINT"
            route.getWaypoints().get(2).waypointType == "REFUEL_PLACE"
            route.getWaypoints().get(3).waypointType == "STOP_PLACE"
            route.getWaypoints().get(4).waypointType == "DESTINATION_PLACE"
    }

    def "Reorder forward"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addStopPlace(new AddStopPlaceCommand(travel.travelId, SOME_LOCATION))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges refuel after checkpoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def refuelWaypointId = route.getWaypoints().get(1).getId()
            def checkpointWaypointId = route.getWaypoints().get(3).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, refuelWaypointId, checkpointWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Refuel is after checkpoint'
            route.getWaypoints().get(0).waypointType == "BEGINNING_PLACE"
            route.getWaypoints().get(1).waypointType == "STOP_PLACE"
            route.getWaypoints().get(2).waypointType == "CHECKPOINT"
            route.getWaypoints().get(3).waypointType == "REFUEL_PLACE"
            route.getWaypoints().get(4).waypointType == "DESTINATION_PLACE"
    }

    def "Reorder backward"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addStopPlace(new AddStopPlaceCommand(travel.travelId, SOME_LOCATION))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges checkpoint after refuel'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def checkpointWaypointId = route.getWaypoints().get(3).getId()
            def refuelWaypointId = route.getWaypoints().get(1).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, checkpointWaypointId, refuelWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Checkpoint is after refuel'
            route.getWaypoints().get(0).waypointType == "BEGINNING_PLACE"
            route.getWaypoints().get(1).waypointType == "REFUEL_PLACE"
            route.getWaypoints().get(2).waypointType == "CHECKPOINT"
            route.getWaypoints().get(3).waypointType == "STOP_PLACE"
            route.getWaypoints().get(4).waypointType == "DESTINATION_PLACE"
    }

    def "Reordering in the same place does't change route"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addStopPlace(new AddStopPlaceCommand(travel.travelId, SOME_LOCATION))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges checkpoint after refuel'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def checkpointWaypointId = route.getWaypoints().get(3).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, checkpointWaypointId, checkpointWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

            route = travelFlowFacade.getRoute(travel.getTravelId())

        then: 'Checkpoint is after refuel'
            route.getWaypoints().get(0).waypointType == "BEGINNING_PLACE"
            route.getWaypoints().get(1).waypointType == "REFUEL_PLACE"
            route.getWaypoints().get(2).waypointType == "STOP_PLACE"
            route.getWaypoints().get(3).waypointType == "CHECKPOINT"
            route.getWaypoints().get(4).waypointType == "DESTINATION_PLACE"
    }

    def "Can't rearrange when rearranging waypoint doesnt exist in route"() {
        setup: 'New travel - checkpoint is after refuel place'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges refuel after checkpoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def checkpointWaypointId = route.getWaypoints().get(2).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, UUID.randomUUID(), checkpointWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Can't rearrange when rearranging after waypoint doesnt exist in route"() {
        setup: 'New travel - checkpoint is after refuel place'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges refuel after checkpoint'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def refuelWaypointId = route.getWaypoints().get(1).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, refuelWaypointId, UUID.randomUUID())

            travelFlowFacade.changeWaypointOrder(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Can't order after destination point"() {
        setup: 'Some travel with route'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            travelFlowFacade.addRefuelPlace(new AddRefuelPlaceCommand(travel.travelId, SOME_LOCATION, CAR_ID))
            travelFlowFacade.addCheckpoint(new AddCheckpointCommand(travel.travelId, SOME_LOCATION))

        when: 'Driver arranges refuel after destination point'
            def route = travelFlowFacade.getRoute(travel.getTravelId())
            def refuelWaypointId = route.getWaypoints().get(1).getId()
            def destinationWaypointId = route.getWaypoints().get(3).getId()
            def command = ChangeOrderWaypointCommand.of(travel.travelId, refuelWaypointId, destinationWaypointId)

            travelFlowFacade.changeWaypointOrder(command)

        then:
            thrown(IllegalArgumentException)
    }

}
