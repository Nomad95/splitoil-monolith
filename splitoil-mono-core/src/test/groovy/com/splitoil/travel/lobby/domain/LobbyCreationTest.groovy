package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.shared.dto.Result
import com.splitoil.travel.lobby.application.CarService
import com.splitoil.travel.lobby.application.LobbyService
import com.splitoil.travel.lobby.application.UserService
import com.splitoil.travel.lobby.domain.model.CarId
import com.splitoil.travel.lobby.domain.model.Driver
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
    private static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')

    private CarId carId

    private LobbyService lobbyService
    private UserService userTranslator
    private CarService carService

    def setup() {
        userTranslator = Stub()
        carService= Stub()
        lobbyService = new LobbyConfiguration().lobbyService(userTranslator, carService)

        userTranslator.currentLoggedDriver() >> new Driver(DRIVER_ID)
    }

    def "Driver should create lobby for new travel"() {
        given:
            driverHasACar()

        and: "Driver does not have another lobby created simultaneously"
            // TODO: check lobby saga

        when: "Driver creates new lobby"
            def lobbyOutput = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))

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
