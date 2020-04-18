package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.shared.model.Currency
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import com.splitoil.travel.lobby.web.dto.*
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to maintain my lobby passengers
""")
@See('resources/cucumber/lobby_creation.feature')
class ParticipantMaintenanceCurrencyTest extends LobbyTest {

    def setup() {
    }

    def "Lobby creator changes currency of particular participant"() {
        setup: 'A new lobby with one car'
            loggedDriver(DRIVER_ID, DRIVER_LOGIN)
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            passengerExists(PASSENGER_1_ID, PASSENGER_NAME, PLN)
            def lobby = lobbyWithCarAndPassenger()

        when: "Lobby creator changes passenger's travel currency"
            def command = ChangeParticipantsTravelCurrencyCommand.of(lobby.lobbyId, PASSENGER_1_ID, EUR)
            def alteredLobby = lobbyService.changeParticipantsCurrency(command)

        then: "Passenger's changed travel currency"
            alteredLobby.participants[1].displayName == PASSENGER_NAME
            alteredLobby.participants[1].travelCurrency == EUR
    }

    def "Passenger has his default currency set when added to lobby"(Currency passengersDefault, String checkCurrency) {
        setup: 'A new lobby with one car and passenger with default currency of'
            loggedDriver(DRIVER_ID, DRIVER_LOGIN)
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            passengerExists(PASSENGER_1_ID, PASSENGER_NAME, passengersDefault.name())

        when: 'Create lobby and add passenger'
            def lobby = lobbyWithCarAndPassenger()

        then: "Passenger's travel currency is his default"
            lobby.participants[1].displayName == PASSENGER_NAME
            lobby.participants[1].travelCurrency == checkCurrency

        where:
            passengersDefault | checkCurrency
            Currency.PLN      | PLN
            Currency.EUR      | EUR
            Currency.USD      | USD
    }

    def "Driver has his default currency set when added to lobby"(Currency driverDefault, String checkCurrency) {
        setup: 'A new lobby with one car and driver with default currency of'
            loggedDriver(DRIVER_ID, DRIVER_LOGIN, driverDefault.name())
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            passengerExists(PASSENGER_1_ID, PASSENGER_NAME, driverDefault.name())

        when: 'Create lobby and add passenger'
            def lobby = lobbyWithCarAndPassenger()

        then: "Driver's travel currency is his default"
            lobby.participants[0].displayName == DRIVER_LOGIN
            lobby.participants[0].travelCurrency == checkCurrency

        where:
            driverDefault | checkCurrency
            Currency.PLN  | PLN
            Currency.EUR  | EUR
            Currency.USD  | USD
    }

    def "Temporal user has his default currency set when added to lobby"(Currency lobbyCurrency, String checkCurrency) {
        setup: 'A new lobby with one car'
            loggedDriver(DRIVER_ID, DRIVER_LOGIN)
            carExists(CAR_ID, DRIVER_ID, 5, 0)
            //passengerExists(PASSENGER_1_ID, PASSENGER_NAME, driverDefault.name())
            lobbyHasDefaultCurrency(lobbyCurrency)

        when: 'Create lobby and add passenger'
            def lobby = lobbyWithCarAndTemporalPassenger()

        then: "Passenger's travel currency is lobby's default"
            lobby.participants[1].displayName == PASSENGER_NAME
            lobby.participants[1].travelCurrency == checkCurrency

        where:
            lobbyCurrency | checkCurrency
            Currency.PLN  | PLN
            Currency.EUR  | EUR
            Currency.USD  | USD
    }

    private def lobbyWithCarAndPassenger() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
        def alteredLobby = lobbyService.addPassenger(AddPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_1_ID, CAR_ID))
        return alteredLobby
    }

    private def lobbyWithCarAndTemporalPassenger() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
        def alteredLobby = lobbyService.addTemporalPassenger(AddTemporalPassengerToLobbyCommand.of(lobby.lobbyId, PASSENGER_NAME, CAR_ID))
        return alteredLobby
    }

    private lobbyHasDefaultCurrency(Currency driverDefault) {
        LobbyConfiguration.StubCurrencyProvider.setDefaultCurrency(driverDefault)
    }

}
