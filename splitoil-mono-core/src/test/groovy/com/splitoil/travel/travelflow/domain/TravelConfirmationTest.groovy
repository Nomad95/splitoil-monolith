package com.splitoil.travel.travelflow.domain

import com.splitoil.UnitTest
import com.splitoil.travel.travelflow.domain.event.CarInitialStateSet
import com.splitoil.travel.travelflow.domain.event.TravelPlanConfirmed
import com.splitoil.travel.travelflow.domain.event.TravelStarted
import com.splitoil.travel.travelflow.web.dto.*
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to confirm travel plan i have made
""")
@See('resources/cucumber/defining_travel.feature')
class TravelConfirmationTest extends TravelTest {

    def "Lobby creator can confirm travel plan"() {
        setup: 'A travel'
            def travel = travelWithTwoCarsAndThreeParticipantsAndDestinationSelected()

        when: 'Lobby creator confirms travel plan'
            def command = ConfirmTravelPlanCommand.of(travel.travelId)
            travelFlowFacade.confirmPlan(command)
            travel = travelFlowFacade.getTravel(travel.travelId)

        then: 'Travel plan confirmed'
            travel.travelStatus == "IN_CONFIRMATION"

            1 * eventPublisher.publish(_ as TravelPlanConfirmed)
    }

    def "Can't confirm when destination not set"() {
        setup: 'A travel'
            def travel = travelWithTwoCarsAndThreeParticipants()
            def command = SelectTravelBeginningCommand.of(travel.travelId, BEGINNING_LOCATION)
            travelFlowFacade.selectTravelBeginning(command)

        when: 'Lobby creator confirms travel plan'
            def confirmCommand = ConfirmTravelPlanCommand.of(travel.travelId)
            travelFlowFacade.confirmPlan(confirmCommand)

        then:
            thrown(IllegalStateException)
    }

    def "Can't confirm when beginning not set"() {
        setup: 'A travel'
            def travel = travelWithTwoCarsAndThreeParticipants()
            def command = SelectTravelDestinationCommand.of(travel.travelId, BEGINNING_LOCATION)
            travelFlowFacade.selectTravelDestination(command)

        when: 'Lobby creator confirms travel plan'
            def confirmCommand = ConfirmTravelPlanCommand.of(travel.travelId)
            travelFlowFacade.confirmPlan(confirmCommand)

        then:
            thrown(IllegalStateException)
    }

    def "Lobby creator have to set car initial state"() {
        setup: 'A confirmed travel'
            def travel = confirmedPlan()
            allPassengersAndCarsExistsInLobby()

        when: 'Lobby creator enters cars fuel level and odometer'
            def command = SetCarInitialStateCommand.of(travel.travelId, CAR_ID, new BigDecimal("15.0"), 123456)
            travelFlowFacade.setCarInitialState(command)

        then: 'Car state entered'
            1 * eventPublisher.publish(_ as CarInitialStateSet)
    }

    def "Throw when adding cars state that is not found in lobby"() {
        setup: 'A confirmed travel'
            def travel = confirmedPlan()
            passengersAreInTheLobbyButNotCar()

        when: 'Lobby creator enters cars fuel level and odometer of wrong car'
            def command = SetCarInitialStateCommand.of(travel.travelId, UUID.randomUUID(), new BigDecimal("15.0"), 123456)
            travelFlowFacade.setCarInitialState(command)

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby creator can start travelling"() {
        setup: 'Travel all set'
            allPassengersAndCarsExistsInLobby()
            def travel = confirmedPlanWithInitialStateSet()

        when: 'Lobby creator starts the travel'
            def command = StartTravelCommand.of(travel.travelId)
            travelFlowFacade.startTravel(command)

        then: 'Travel started'
            1 * eventPublisher.publish(_ as TravelStarted)
    }

    def "Starting travel changes travel status"() {
        setup: 'Travel all set'
            allPassengersAndCarsExistsInLobby()
            def travel = confirmedPlanWithInitialStateSet()

        when: 'Lobby creator starts the travel'
            def command = StartTravelCommand.of(travel.travelId)
            travelFlowFacade.startTravel(command)
            travel = travelFlowFacade.getTravel(travel.travelId)

        then: 'Travel status is changed'
            travel.travelStatus == "IN_TRAVEL"
    }

    def "Cant start travel when confirmed but not set cars initial state"() {
        setup: 'Confirmed travel'
            allPassengersAndCarsExistsInLobby()
            def travel = confirmedPlan()

        when: 'Lobby creator starts the travel'
            def command = StartTravelCommand.of(travel.travelId)
            travelFlowFacade.startTravel(command)

        then:
            thrown(IllegalStateException)
    }

}
