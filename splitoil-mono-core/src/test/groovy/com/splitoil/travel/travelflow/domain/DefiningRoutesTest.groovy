package com.splitoil.travel.travelflow.domain

import com.splitoil.UnitTest
import com.splitoil.travel.travelflow.domain.event.*
import com.splitoil.travel.travelflow.web.dto.*
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to define the travel route to show it to the participants
""")
@See('resources/cucumber/defining_travel.feature')
class DefiningRoutesTest extends TravelTest {

    def "Lobby creator can select travel beginning"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipants()

        when: 'Lobby creator selects travel beginning'
            def command = SelectTravelBeginningCommand.of(travel.travelId, BEGINNING_LOCATION)
            travelFlowFacade.selectTravelBeginning(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Travel beginning set'
            route.getWaypoints().size() == 1
            route.getWaypoints().get(0).getWaypointType() == "BEGINNING_PLACE"

            1 * eventPublisher.publish(_ as TravelBeginningPlaceSelected)
    }

    def "Lobby creator can select travel destination"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipants()

        when: 'Lobby creator selects travel destination'
            def command = SelectTravelDestinationCommand.of(travel.travelId, DESTINATION_LOCATION)
            travelFlowFacade.selectTravelDestination(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Travel destination set'
            route.getWaypoints().size() == 1
            route.getWaypoints().get(0).getWaypointType() == "DESTINATION_PLACE"

            1 * eventPublisher.publish(_ as TravelDestinationPlaceSelected)
    }

    def "Lobby creator can select reseat place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            allPassengersAndCarsExistsInLobby()

        when: 'Lobby creator selects reseat place'
            def command = AddReseatPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, CAR_ID, SECOND_CAR_ID, PASSENGER_1_ID)
            travelFlowFacade.addReseatPlace(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Reseat place set'
            route.getWaypoints().size() == 3
            route.getWaypoints().get(1).getWaypointType() == "RESEAT_PLACE"

            1 * eventPublisher.publish(_ as TravelReseatPlaceAdded)
    }

    def "Should throw when car is not found in the lobby"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            passengersAreInTheLobbyButNotCar()

        when: 'Lobby creator selects reseat place but with car i that is not in the lobby'
            def command = AddReseatPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, UUID.randomUUID(), SECOND_CAR_ID, PASSENGER_1_ID)
            travelFlowFacade.addReseatPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Should throw when passenger is not found in the lobby"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            passengersAreInTheLobbyButNotCar()

        when: 'Lobby creator selects reseat place but with car i that is not in the lobby'
            def command = AddReseatPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, CAR_ID, SECOND_CAR_ID, UUID.randomUUID())
            travelFlowFacade.addReseatPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can select stop place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()

        when: 'Lobby creator selects stop place'
            def command = AddStopPlaceCommand.of(travel.travelId, DESTINATION_LOCATION)
            travelFlowFacade.addStopPlace(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Stop place set'
            route.getWaypoints().size() == 3
            route.getWaypoints().get(1).getWaypointType() == "STOP_PLACE"

            1 * eventPublisher.publish(_ as TravelStopPlaceAdded)
    }

    def "Lobby creator can select refuel place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            allPassengersAndCarsExistsInLobby()

        when: 'Lobby creator selects refuel place'
            def command = AddRefuelPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, CAR_ID)
            travelFlowFacade.addRefuelPlace(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Refuel place set'
            route.getWaypoints().size() == 3
            route.getWaypoints().get(1).getWaypointType() == "REFUEL_PLACE"

            1 * eventPublisher.publish(_ as TravelRefuelPlaceAdded)
    }

    def "Should throw when car is not in the lobby when adding refuel car waypoint"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            passengersAreInTheLobbyButNotCar()

        when: 'Lobby creator selects refuel place but the refueled car doesnt exist'
            def command = AddRefuelPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, UUID.randomUUID())
            travelFlowFacade.addRefuelPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can select passenger boarding place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            allPassengersAndCarsExistsInLobby()

        when: 'Lobby creator selects passenger boarding place'
            def command = AddParticipantBoardingPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, PASSENGER_1_ID, CAR_ID)
            travelFlowFacade.addBoardingPlace(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Passenger boarding place set'
            route.getWaypoints().size() == 3
            route.getWaypoints().get(1).getWaypointType() == "PARTICIPANT_BOARDING_PLACE"

            1 * eventPublisher.publish(_ as TravelParticipantBoardingPlaceAdded)
    }

    def "Should throw when participant is not found in the lobby when adding boarding place waypoint"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            allCarsArePresentInLobbyButNotPassengers()

        when: 'Lobby creator selects passenger boarding place but participant is not in the lobby'
            def command = AddParticipantBoardingPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, UUID.randomUUID(), CAR_ID)
            travelFlowFacade.addBoardingPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Should throw when car is not found in the lobby when adding boarding place waypoint"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            passengersAreInTheLobbyButNotCar()

        when: 'Lobby creator selects passenger boarding place but car is not in the lobby'
            def command = AddParticipantBoardingPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, PASSENGER_1_ID, UUID.randomUUID())
            travelFlowFacade.addBoardingPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can select passenger exit place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            allPassengersAndCarsExistsInLobby()

        when: 'Lobby creator selects passenger exit place'
            def command = AddParticipantExitPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, PASSENGER_1_ID, CAR_ID)
            travelFlowFacade.addExitPlace(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Passenger exit place set'
            route.getWaypoints().size() == 3
            route.getWaypoints().get(1).getWaypointType() == "PARTICIPANT_EXIT_PLACE"

            1 * eventPublisher.publish(_ as TravelParticipantExitPlaceAdded)
    }

    def "Should throw when car is not found in the lobby when adding exit place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            passengersAreInTheLobbyButNotCar()

        when: 'Lobby creator selects passenger exit place but car is not present'
            def command = AddParticipantExitPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, PASSENGER_1_ID, UUID.randomUUID())
            travelFlowFacade.addExitPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Should throw when participant is not found in the lobby when adding exit place"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()
            allCarsArePresentInLobbyButNotPassengers()

        when: 'Lobby creator selects passenger exit place but participant is not present'
            def command = AddParticipantExitPlaceCommand.of(travel.travelId, DESTINATION_LOCATION, UUID.randomUUID(), CAR_ID)
            travelFlowFacade.addExitPlace(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can select checkpoint"() {
        setup: 'Travel with beginning and destination point'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()

        when: 'Lobby creator selects checkpoint'
            def command = AddCheckpointCommand.of(travel.travelId, DESTINATION_LOCATION)
            travelFlowFacade.addCheckpoint(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Checkpoint set'
            route.getWaypoints().size() == 3
            route.getWaypoints().get(1).getWaypointType() == "CHECKPOINT"

            1 * eventPublisher.publish(_ as TravelCheckpointAdded)
    }


}
