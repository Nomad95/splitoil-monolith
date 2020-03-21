package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.shared.dto.Result
import com.splitoil.travel.lobby.application.LobbyService
import com.splitoil.travel.lobby.domain.model.CarId
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See
import spock.lang.Specification

@Category(UnitTest)
@Narrative("""
As a driver i want to create new lobby for my new travel
""")
@See('resources/cucumber/lobby_creation.feature')
class LobbyCreationTest extends Specification {

    private static final String LOBBY_NAME = "Some lobby name"
    private static final Long DRIVER_ID = 1L

    private CarId carId

    private LobbyService lobbyService;

    def setup() {
        lobbyService = new LobbyConfiguration().lobbyService()
    }

    def "Driver should create lobby for new travel"() {
        given:
            driverHasACar()

        and: "Driver does not have another lobby created simultaneously"
            // TODO: check lobby saga

        when: "Driver creates new lobby"
            def lobbyOutput = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME, DRIVER_ID))

        then:
            lobbyOutput.lobbyId != null
            lobbyOutput.lobbyStatus == "IN_CREATION"

        when: "Driver has chosen a car"
            def result = lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobbyOutput.lobbyId, carId.carId))
            def finalLobbyOutput = lobbyService.getLobby(lobbyOutput.lobbyId)

        then: "Car is added to lobby"
            result == Result.Success

        and: "Lobby is ready for configuration"
            finalLobbyOutput.lobbyStatus == "IN_CONFIGURATION"
    }

    private def driverHasACar() {
        carId = CarId.of(UUID.randomUUID())
    }
}
