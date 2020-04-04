package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.shared.dto.Result
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
            addCar(CAR_ID, DRIVER_ID, 5, 1)

        and: "Driver does not have another lobby created simultaneously"
            // TODO: check lobby saga

        when: "Driver creates new lobby"
            def lobbyOutput = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))

        then:
            lobbyOutput.lobbyId != null
            lobbyOutput.lobbyStatus == "IN_CREATION"

        when: "Driver has chosen a car"
            def result = lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobbyOutput.lobbyId, CAR_ID))
            def finalLobbyOutput = lobbyService.getLobby(lobbyOutput.lobbyId)

        then: "Car is added to lobby"
            result == Result.Success

        and: "Lobby is ready for configuration"
            finalLobbyOutput.lobbyStatus == "IN_CONFIGURATION"
    }

}
