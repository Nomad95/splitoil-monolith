package com.splitoil.travel.lobby.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.application.CarService
import com.splitoil.travel.lobby.application.LobbyService
import com.splitoil.travel.lobby.application.UserService
import com.splitoil.travel.lobby.domain.model.CarId
import com.splitoil.travel.lobby.domain.model.Driver
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.ChangeTravelDefaultCurrencyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import com.splitoil.travel.lobby.web.dto.SetTravelTopRatePer1kmCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See
import spock.lang.Specification

@Category(UnitTest)
@Narrative("""
As a driver i want to configure my newly created lobby
""")
@See('resources/cucumber/lobby_creation.feature')
class LobbyConfigurationTest extends Specification {

    private static final String LOBBY_NAME = "Some lobby name"
    private static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    public static final String USD = 'USD'
    public static final String PLN = 'PLN'

    private LobbyService lobbyService
    private UserService userTranslator
    private CarService carService

    def setup() {
        userTranslator = Stub()
        carService = Stub()
        lobbyService = new LobbyConfiguration().lobbyService(userTranslator, carService)

        userTranslator.currentLoggedDriver() >> new Driver(DRIVER_ID)
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

    //TODO will change
    def "Travel currency should be set to driver default currency when is newly created"() {
        given:
            def lobby = aNewLobby()

        expect: 'Travel currency is set to driver default'
            lobby.travelCurrency == PLN
    }

    //not setting top rate makes that price can be anything later and null rate should be handled
    //should get default driver currency for travel

    private def aNewLobby() {
        def carId = CarId.of(UUID.randomUUID())
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))
        lobbyService.addCarToLobby(AddCarToTravelCommand.of(lobby.lobbyId, carId.carId))

        return lobby;
    }

    private def carlessLobby() {
        def lobby = lobbyService.createLobby(CreateLobbyCommand.of(LOBBY_NAME))

        return lobby;
    }

}
