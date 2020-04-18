package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.AddPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.AddTemporalPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to add travel participants to my lobby
""")
@See('resources/cucumber/lobby_creation.feature')
class ParticipantAddingTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
        passengerExists(PASSENGER_1_ID, PASSENGER_NAME, PLN)
    }

    def "Lobby creator adds a passenger to travel lobby"() {
        setup: 'A new lobby'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID)
            def alteredLobby = lobbyService.addPassenger(command)

        then: 'Lobby has a passenger'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 2
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            alteredLobby.participants[1].participantType == 'PASSENGER'
    }

    def "Lobby creator adds an ad hoc passenger to travel lobby"() {
        setup: 'A new lobby'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds passenger that is not registered in the app'
            def command = AddTemporalPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_NAME, CAR_ID)
            def alteredLobby = lobbyService.addTemporalPassenger(command)

        then: 'Lobby has a temporal passenger'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 2
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            alteredLobby.participants[1].participantType == 'TEMPORAL_PASSENGER'
    }

    def "Should throw when creator adds a passenger to car that is full"() {
        setup: 'A new lobby'
            carExists(CAR_ID, DRIVER_ID, 5, 4)//TODO ogarnac liczby
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID)
            lobbyService.addPassenger(command)

        then:
            thrown(IllegalStateException.class)
    }

    def "Should throw when creator adds a passenger to car that is not present in lobby"() {
        setup: 'A new lobby'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, UUID.randomUUID())
            lobbyService.addPassenger(command)

        then:
            thrown(IllegalStateException.class)
    }

    def "Should throw when creator adds a same passenger twice"() {
        setup: 'A new lobby'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID)
            lobbyService.addPassenger(command)
            lobbyService.addPassenger(command)

        then:
            thrown(IllegalStateException.class)
    }

    private def aNewLobbyWithOneCar() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))

        return lobby;
    }

}
