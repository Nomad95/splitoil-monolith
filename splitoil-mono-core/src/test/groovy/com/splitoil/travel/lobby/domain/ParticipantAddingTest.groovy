package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.application.LobbyService
import com.splitoil.travel.lobby.application.UserTranslator
import com.splitoil.travel.lobby.domain.model.CarId
import com.splitoil.travel.lobby.domain.model.Driver
import com.splitoil.travel.lobby.domain.model.Participant
import com.splitoil.travel.lobby.domain.model.ParticipantType
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.AddExternalPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.AddPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See
import spock.lang.Specification

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to add travel participants
""")
@See('resources/cucumber/lobby_creation.feature')
class ParticipantAddingTest extends Specification {

    private static final String LOBBY_NAME = "Some lobby name"
    private static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    private static final UUID PASSENGER_1_ID = UUID.fromString('1a26cbe6-1c70-42a2-8734-6b861702672d')
    private static final String PASSENGER_NAME = 'Wojtaszko'

    private LobbyService lobbyService
    private UserTranslator userTranslator

    def setup() {
        userTranslator = Stub()
        lobbyService = new LobbyConfiguration().lobbyService(userTranslator)

        userTranslator.currentLoggedDriver() >> new Driver(DRIVER_ID)
        userTranslator.getPassenger(_) >> new Participant(PASSENGER_1_ID, PASSENGER_NAME, ParticipantType.PASSENGER)
    }

    def "Lobby creator adds a passenger to travel lobby"() {
        given: 'A new lobby'
            def lobby = aNewLobby()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyService.addPassenger(command)

        then: 'Lobby has a passenger'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 1
            alteredLobby.participants[0].displayName == PASSENGER_NAME
            alteredLobby.participants[0].participantType == 'PASSENGER'
    }

    def "Lobby creator adds an ad hoc passenger to travel lobby"() {
        given: 'A new lobby'
            def lobby = aNewLobby()

        when: 'Lobby creator adds passenger that is not registered in the app'
            def command = AddExternalPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_NAME)
            def alteredLobby = lobbyService.addExternalPassenger(command)

        then: 'Lobby has a new passenger'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 1
            alteredLobby.participants[0].displayName == PASSENGER_NAME
            alteredLobby.participants[0].participantType == 'AD_HOC_PASSENGER'
    }

    private def aNewLobby() {
        def carId = CarId.of(UUID.randomUUID())
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, carId.carId))

        return lobby;
    }

}
