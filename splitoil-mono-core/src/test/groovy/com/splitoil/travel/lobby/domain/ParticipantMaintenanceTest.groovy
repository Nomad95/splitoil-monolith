package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.domain.event.ParticipantCostChargingChanged
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.AddPassengerToLobbyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import com.splitoil.travel.lobby.web.dto.ToggleParticipantsCostChargingCommand
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
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Lobby creator disables passenger from cost charging'
            def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyFacade.toggleCostCharging(command)

        then: "Passenger's disabled from cost charging"
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            !alteredLobby.participants[1].costChargingEnabled

            1 * eventPublisher.publish(_ as ParticipantCostChargingChanged)
    }

    def "Lobby creator enables particular participant from cost charging"() {
        setup: 'A new lobby with one car'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = passengerHasDisabledCostCharging()

        when: 'Lobby creator enables passenger from cost charging'
            def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyFacade.toggleCostCharging(command)

        then: "Passenger's enabled from cost charging"
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            alteredLobby.participants[1].costChargingEnabled

            1 * eventPublisher.publish(_ as ParticipantCostChargingChanged)
    }

    def "Disabling particular participant from cost charging does not affect others"() {
        setup: 'A new lobby with one car'
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            def lobby = lobbyWithCarAndPassenger()

        when: 'Lobby creator disables passenger from cost charging'
            def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
            def alteredLobby = lobbyFacade.toggleCostCharging(command)

        then: 'Other not changed'
            alteredLobby.participants[0].costChargingEnabled

            1 * eventPublisher.publish(_ as ParticipantCostChargingChanged)
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

    private def passengerHasDisabledCostCharging() {
        def lobby = lobbyWithCarAndPassenger()
        def command = ToggleParticipantsCostChargingCommand.of(lobby.lobbyId, PASSENGER_1_ID)
        def alteredLobby = lobbyFacade.toggleCostCharging(command)

        return alteredLobby
    }

}
