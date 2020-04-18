package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.shared.model.Currency
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.ChangeTravelDefaultCurrencyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import com.splitoil.travel.lobby.web.dto.SetTravelTopRatePer1kmCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a driver i want to configure my newly created lobby
""")
@See('resources/cucumber/lobby_creation.feature')
class LobbyConfigurationTest extends LobbyTest {

    def setup() {
        loggedDriver(DRIVER_ID, DRIVER_LOGIN)
        carExists(CAR_ID, DRIVER_ID, 5, 1)
    }

    def "Driver configures top rate per 1 km lobby for new travel"() {
        given:
            def lobby = aNewLobby()

        when: 'Driver sets top rate per 1km to'
            lobby = lobbyService.setTravelTopRatePer1km(SetTravelTopRatePer1kmCommand.of(lobby.lobbyId, new BigDecimal("3.50")))

        then: 'Rate is visible'
            lobby.topRatePer1km == new BigDecimal("3.50")
    }

    def "Lobby cannot be configured for top rate per 1km before adding the first car"() {
        given:
            def lobby = carlessLobby()

        when:
            lobbyService.setTravelTopRatePer1km(SetTravelTopRatePer1kmCommand.of(lobby.lobbyId, new BigDecimal("3.50")))

        then:
            thrown(IllegalStateException)
    }

    def "Lobby cannot be configured with non-positive top rate per 1km "() {
        given:
            def lobby = aNewLobby()

        when:
            lobbyService.setTravelTopRatePer1km(SetTravelTopRatePer1kmCommand.of(lobby.lobbyId, new BigDecimal("-3.50")))

        then:
            thrown(IllegalArgumentException)
    }

    def "Lobby cannot be configured with zero top rate per 1km "() {
        given:
            def lobby = aNewLobby()

        when:
            lobbyService.setTravelTopRatePer1km(SetTravelTopRatePer1kmCommand.of(lobby.lobbyId, new BigDecimal("0")))

        then:
            thrown(IllegalArgumentException)
    }

    def "Driver configures travel currency in lobby"() {
        given:
            def lobby = aNewLobby()

        when: 'Driver sets travel currency'
            lobby = lobbyService.changeTravelDefaultCurrency(ChangeTravelDefaultCurrencyCommand.of(lobby.lobbyId, USD))

        then: 'Travel currency is visible'
            lobby.travelCurrency == USD
    }

    def "Lobby cannot be configured for currency before adding the first car"() {
        given:
            def lobby = carlessLobby()

        when:
            lobbyService.changeTravelDefaultCurrency(ChangeTravelDefaultCurrencyCommand.of(lobby.lobbyId, USD))

        then:
            thrown(IllegalStateException)
    }

    def "Travel currency should be set to driver default currency when is newly created"(Currency setCurrency, String checkCurrency) {
        given: 'Set explicitly driver default currency'
            LobbyConfiguration.StubCurrencyProvider.setDefaultCurrency(setCurrency)

        when: 'Create new lobby'
            def lobby = aNewLobby()

        then: 'Travel currency is set to driver default'
            lobby.travelCurrency == checkCurrency

        where:
            setCurrency  | checkCurrency
            Currency.PLN | PLN
            Currency.EUR | EUR
            Currency.USD | USD
    }

    def "Driver is present as a participant when added a car to lobby"() {
        setup: 'A new lobby'
            def lobby = carlessLobby()

        when: 'Adding a car to lobby'
            lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))
            def alteredLobby = lobbyService.getLobby(lobby.lobbyId)

        then: 'Driver is added as a participant'
            alteredLobby.lobbyId == lobby.lobbyId
            alteredLobby.participants.size() == 1
            alteredLobby.participants[0].displayName == DRIVER_LOGIN
            alteredLobby.participants[0].participantType == 'CAR_DRIVER'
    }

    //not setting top rate makes that price can be anything later and null rate should be handled
    //should get default driver currency for travel

    private def aNewLobby() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, CAR_ID, DRIVER_ID))

        return lobby;
    }

    private def carlessLobby() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))

        return lobby;
    }

}
