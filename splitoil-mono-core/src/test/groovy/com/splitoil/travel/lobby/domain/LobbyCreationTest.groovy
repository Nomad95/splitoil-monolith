package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a driver i want to create new lobby for my new travel
""")
@See('resources/cucumber/lobby_creation.feature')
class LobbyCreationTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
    }

    def "Driver should create lobby for new travel"() {
        given:
            carExists(CAR_ID, DRIVER_ID, 5, 0)

        and: "Driver does not have another lobby created simultaneously"
            // TODO: ???

        when: "Driver creates new lobby"
            def lobbyOutput = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))

        then:
            lobbyOutput.lobbyId != null
            lobbyOutput.lobbyStatus == "IN_CREATION"

        when: "Driver has chosen a car"
            def result = lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobbyOutput.lobbyId, CAR_ID, DRIVER_ID))
            def finalLobbyOutput = lobbyService.getLobby(lobbyOutput.lobbyId)

        then: "Car is added to lobby"
            result.cars[0].id == CAR_ID

        then: "Lobby is ready for configuration"
            finalLobbyOutput.lobbyStatus == "IN_CONFIGURATION"
    }

}
