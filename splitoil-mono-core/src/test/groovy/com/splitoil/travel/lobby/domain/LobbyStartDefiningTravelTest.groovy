package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested
import com.splitoil.travel.lobby.domain.model.LobbyNotReadyForTravelException
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.AddPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import com.splitoil.travel.lobby.web.dto.StartDefiningTravelPlanCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to reseat my lobby passengers
""")
@See('resources/cucumber/lobby_creation.feature')
class LobbyStartDefiningTravelTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
        passengerExists(PASSENGER_1_ID, PASSENGER_NAME, PLN)
        passengerExists(PASSENGER_2_ID, PASSENGER_2_NAME, PLN)
        passengerExists(PASSENGER_3_ID, PASSENGER_3_NAME, PLN)
    }

    def "Lobby creator can start defining travelling plan"() {
        setup: 'lobby with two cars and three passengers'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndThreePassengers()

        when: 'Starting defining travelling plan'
            def command = StartDefiningTravelPlanCommand.of(lobby.lobbyId)
            lobbyFacade.startDefiningTravelPlan(command)

        then: 'Travel has started creating'
            1 * eventPublisher.publish(_ as TravelCreationRequested)
    }

    def "Lobby creator can't start defining travelling plan when lobby is not in configuration mode"() {
        setup: 'Brand new lobby'
            def lobby = lobbyFacade.createLobby(CreateLobbyCommand.of(LOBBY_NAME))

        when: 'Starting travel'
            def command = StartDefiningTravelPlanCommand.of(lobby.lobbyId)
            lobbyFacade.startDefiningTravelPlan(command)

        then:
            thrown(LobbyNotReadyForTravelException)
    }

    def "Lobby should receive travelId after it is created"() {
        setup: 'Some lobby'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndThreePassengers()

        when: 'Receive travelId'
            lobbyFacade.assignTravelToLobby(TRAVEL_ID, lobby.lobbyId)
            lobby = lobbyFacade.getLobby(lobby.lobbyId)

        then:
            lobby.travelId == TRAVEL_ID
    }

    private def lobbyWithTwoCarsAndThreePassengers() {
        def lobby = lobbyFacade.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyFacade.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
        lobbyFacade.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, SECOND_CAR_ID, SECOND_DRIVER_ID))
        lobbyFacade.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID))
        lobbyFacade.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_2_ID, SECOND_CAR_ID))
        def alteredLobby = lobbyFacade.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_3_ID, CAR_ID))
        return alteredLobby
    }


}
