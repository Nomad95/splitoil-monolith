package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.web.dto.*
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to maintain my lobby passengers
""")
@See('resources/cucumber/lobby_creation.feature')
class ParticipantMaintenanceTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
        passengerExists(PASSENGER_1_ID, PASSENGER_NAME, PLN)
    }

    def "Lobby creator disables particular participant from cost charging"() {
        setup: 'A new lobby with one car'
            carExists(CAR_ID, DRIVER_ID, 5, 1)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Lobby creator disables passenger from cost charging'
            def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyService.toggleCostCharging(command)

        then: "Passenger's disabled from cost charging"
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            !alteredLobby.participants[1].costChargingEnabled

    }

    def "Lobby creator enables particular participant from cost charging"() {
        setup: 'A new lobby with one car'
            carExists(CAR_ID, DRIVER_ID, 5, 1)
            def lobby = passengerHasDisabledCostCharging()

        when: 'Lobby creator enables passenger from cost charging'
            def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyService.toggleCostCharging(command)

        then: "Passenger's enabled from cost charging"
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            alteredLobby.participants[1].costChargingEnabled
    }

    def "Disabling particular participant from cost charging does not affect others"() {
        setup: 'A new lobby with one car'
            carExists(CAR_ID, DRIVER_ID, 5, 1)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Lobby creator disables passenger from cost charging'
            def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyService.toggleCostCharging(command)

        then: 'Other not changed'
            alteredLobby.participants[0].costChargingEnabled
    }

    def "Lobby creator can assign participant to another car"() {
        setup: 'lobby with two cars and one passenger'
            carExists(CAR_ID, DRIVER_ID, 5, 1)
            carExists(SECOND_CAR_ID, SECOND_DRIVER_ID, 5, 1)
            driverExists(SECOND_DRIVER_ID, SECOND_DRIVER_LOGIN)
            def lobby = lobbyWithTwoCarsAndOnePassenger()

        when: 'Assign passenger to another car'
            def command = AssignToCarCommand.of(lobby.lobbyId, PASSENGER_1_ID, SECOND_CAR_ID)
            def alteredLobby = lobbyService.assignToCar(command)

        then: 'passenger is in another car'
            alteredLobby.participants[0].assignedCar == SECOND_CAR_ID
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

    private def passengerHasDisabledCostCharging() {
        def lobby = lobbyWithCarAndPassenger()
        def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
        def alteredLobby = lobbyService.toggleCostCharging(command)

        return alteredLobby
    }

}
