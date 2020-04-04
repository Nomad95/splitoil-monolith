package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.application.CarService
import com.splitoil.travel.lobby.application.LobbyService
import com.splitoil.travel.lobby.application.UserService
import com.splitoil.travel.lobby.domain.model.*
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
    private static final UUID CAR_ID = UUID.fromString('1ad11d82-5f49-4761-abc3-f8cd9b9bd594')
    private static final String PASSENGER_NAME = 'Wojtaszko'

    private LobbyService lobbyService
    private UserService userTranslator
    private CarService carService

    def setup() {
        userTranslator = Stub()
        carService = Stub()
        lobbyService = new LobbyConfiguration().lobbyService(userTranslator, carService)

        userTranslator.currentLoggedDriver() >> new Driver(DRIVER_ID)
        userTranslator.getPassenger(_) >> new Participant(PASSENGER_1_ID, PASSENGER_NAME, ParticipantType.PASSENGER)
    }

    def "Lobby creator adds a passenger to travel lobby"() {
        setup: 'A new lobby'
            withCar(CAR_ID, DRIVER_ID, 5, 1)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID)
            def alteredLobby = lobbyService.addPassenger(command)

        then: 'Lobby has a passenger'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 1
            alteredLobby.participants[0].displayName == PASSENGER_NAME
            alteredLobby.participants[0].participantType == 'PASSENGER'
    }

    def "Lobby creator adds an ad hoc passenger to travel lobby"() {
        setup: 'A new lobby'
            withCar(CAR_ID, DRIVER_ID, 5, 1)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds passenger that is not registered in the app'
            def command = AddExternalPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_NAME, CAR_ID)
            def alteredLobby = lobbyService.addExternalPassenger(command)

        then: 'Lobby has a temporal passenger'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 1
            alteredLobby.participants[0].displayName == PASSENGER_NAME
            alteredLobby.participants[0].participantType == 'TEMPORAL_PASSENGER'
    }

    def "Should throw when creator adds a passenger to car that is full"() {
        setup: 'A new lobby'
            withCar(CAR_ID, DRIVER_ID, 5, 5)
            def lobby = aNewLobbyWithOneCar()

        when: 'Lobby creator adds a passenger'
            def command = AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID)
            lobbyService.addPassenger(command)

        then:
            thrown(IllegalStateException.class)
    }

    private def aNewLobbyWithOneCar() {
        def carId = CarId.of(CAR_ID)
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, carId.carId))

        return lobby;
    }

    private def withCar(final UUID carId, final UUID carOwner, final int seats, final int occupiedSeats) {
        carService.getCar(carId) >> new Car(CarId.of(carId), carOwner, seats, occupiedSeats)
    }

}
