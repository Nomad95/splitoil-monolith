package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.AddPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import com.splitoil.travel.lobby.web.dto.RemoveParticipantFromLobbyCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to remove my lobby participants
""")
@See('resources/cucumber/lobby_creation.feature')
class ParticipantRemovingTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
        passengerExists(PASSENGER_1_ID, PASSENGER_NAME, PLN)
    }

    def "Lobby creator can remove participant from lobby"() {
        setup: 'lobby with car and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Lobby creator removes passenger'
            def command = RemoveParticipantFromLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyService.removeFromLobby(command)

        then: 'Passenger is no longer in lobby'
            alteredLobby.participants.size() == 1 //driver only
    }

    def "Lobby consistent when removing participant"() {
        setup: 'lobby with car and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Lobby creator removes passenger'
            def command = RemoveParticipantFromLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyService.removeFromLobby(command)

        then: 'Cars seats state is correct'
            alteredLobby.cars[0].seatsOccupied == 1 //only driver
    }

    def "Removing driver removes all passengers and car from lobby"() {
        setup: 'lobby with two cars and one passenger in second car'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassengerInSecondCar()

        when: 'Lobby creator removes driver'
            def command = RemoveParticipantFromLobbyCommand.of(lobby.lobbyId, SECOND_DRIVER_ID)
            def alteredLobby = lobbyService.removeFromLobby(command)

        then: 'Second car is removes as well as passenger'
            alteredLobby.cars.size() == 1 //only lobby creators car
            alteredLobby.participants.size() == 1 //only driver from 1st car
    }

    def "Cannot remove lobby creator"() {
        setup: 'lobby with two cars and one passenger in second car'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 0)
            driverExists(DRIVER_ID, DRIVER_LOGIN)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassengerInSecondCar()

        when: 'Lobby creator removes himself'
            def command = RemoveParticipantFromLobbyCommand.of(lobby.lobbyId, DRIVER_ID)
            lobbyService.removeFromLobby(command)

        then:
            thrown(IllegalArgumentException)
    }

    private def aNewLobbyWithOneCar() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))

        return lobby;
    }

    private def lobbyWithCarAndPassenger() {
        def lobby = aNewLobbyWithOneCar()
        def alteredLobby = lobbyService.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID))
        return alteredLobby
    }

    private def lobbyWithTwoCarsAndOnePassenger() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, SECOND_CAR_ID, SECOND_DRIVER_ID))
        def alteredLobby = lobbyService.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID))
        return alteredLobby
    }

    private def lobbyWithTwoCarsAndOnePassengerInSecondCar() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, SECOND_CAR_ID, SECOND_DRIVER_ID))
        def alteredLobby = lobbyService.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, SECOND_CAR_ID))
        return alteredLobby
    }


}
