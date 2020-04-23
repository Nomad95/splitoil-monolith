package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.AddPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.AssignToCarCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to reseat my lobby passengers
""")
@See('resources/cucumber/lobby_creation.feature')
class ParticipantReseatingTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
        passengerExists(PASSENGER_1_ID, PASSENGER_NAME, PLN)
    }

    def "Lobby creator can assign participant to another car"() {
        setup: 'lobby with two cars and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassenger()

        when: 'Assign passenger to another car'
            def command = AssignToCarCommand.of(lobby.lobbyId, PASSENGER_1_ID, SECOND_CAR_ID)
            def alteredLobby = lobbyFacade.assignToCar(command)

        then: 'passenger is in another car'
            alteredLobby.participants[2].assignedCar == SECOND_CAR_ID
    }

    def "Seats are consistent after reseating"() {
        setup: 'lobby with two cars and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0) //0 at creation, 1 with driver, 2 at adding first passenger
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassenger()

        when: 'Assign passenger to another car'
            def command = AssignToCarCommand.of(lobby.lobbyId, PASSENGER_1_ID, SECOND_CAR_ID)
            def alteredLobby = lobbyFacade.assignToCar(command)

        then: 'first car has one place more second one place less'
            alteredLobby.cars[0].seatsOccupied == 1
            alteredLobby.cars[1].seatsOccupied == 2
    }

    def "Lobby creator can't assign participant to another car when is full"() {
        setup: 'lobby with two cars and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 4)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassenger()

        when: 'Assign passenger to another car'
            def command = AssignToCarCommand.of(lobby.lobbyId, PASSENGER_1_ID, SECOND_CAR_ID)
            lobbyFacade.assignToCar(command)

        then:
            thrown(IllegalStateException)
    }

    def "Lobby creator can't assign participant to non-existing car"() {
        setup: 'lobby with one car and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Assign passenger to another car'
            def command = AssignToCarCommand.of(lobby.lobbyId, PASSENGER_1_ID, SECOND_CAR_ID)
            lobbyFacade.assignToCar(command)

        then:
            thrown(IllegalStateException)
    }

    def "Lobby creator can't reseat driver"() {
        setup: 'lobby with two cars and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassenger()

        when: 'Assign passenger to another car'
            def command = AssignToCarCommand.of(lobby.lobbyId, DRIVER_ID, SECOND_CAR_ID)
            lobbyFacade.assignToCar(command)

        then:
            thrown(IllegalStateException)
    }

    private def aNewLobbyWithOneCar() {
        def lobby = lobbyFacade.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyFacade.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))

        return lobby;
    }

    private def lobbyWithCarAndPassenger() {
        def lobby = aNewLobbyWithOneCar()
        def alteredLobby = lobbyFacade.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID))
        return alteredLobby
    }

    private def lobbyWithTwoCarsAndOnePassenger() {
        def lobby = lobbyFacade.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyFacade.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
        lobbyFacade.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, SECOND_CAR_ID, SECOND_DRIVER_ID))
        def alteredLobby = lobbyFacade.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID))
        return alteredLobby
    }


}
