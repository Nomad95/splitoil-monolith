package com.splitoil.travel.lobby.domain

import com.splitoil.shared.event.EventPublisher
import com.splitoil.shared.model.Currency
import com.splitoil.travel.lobby.application.CarTranslationService
import com.splitoil.travel.lobby.application.LobbyFacade
import com.splitoil.travel.lobby.application.UserTranslationService
import com.splitoil.travel.lobby.domain.model.*
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import spock.lang.Specification

class LobbyTest extends Specification {
    protected static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    protected static final UUID SECOND_DRIVER_ID = UUID.fromString('25151ea3-239d-453f-bc49-d0345d9210bf')
    protected static final UUID CAR_ID = UUID.fromString('1ad11d82-5f49-4761-abc3-f8cd9b9bd594')
    protected static final UUID SECOND_CAR_ID = UUID.fromString('29c9c416-cad6-4845-97a0-3d2b1bf496e9')
    protected static final UUID PASSENGER_1_ID = UUID.fromString('1a26cbe6-1c70-42a2-8734-6b861702672d')
    protected static final UUID PASSENGER_2_ID = UUID.fromString('46e853a4-bb2e-43ab-8e88-80807468954d')
    protected static final UUID PASSENGER_3_ID = UUID.fromString('4dbd718c-f9b1-4c65-9b3c-0af8e5eaf5d1')
    protected static final UUID TRAVEL_ID = UUID.fromString('1511e033-5db3-4e4d-a498-7e7f32cfc063')

    protected static final String LOBBY_NAME = "Some lobby name"
    protected static final String PASSENGER_NAME = 'Wojtaszko'
    protected static final String PASSENGER_2_NAME = 'Wantajasz'
    protected static final String PASSENGER_3_NAME = 'Wojan'
    protected static final String DRIVER_LOGIN = 'Wojtapsiux'
    protected static final String SECOND_DRIVER_LOGIN = 'Wantajaks'

    protected static final String PLN = 'PLN'
    protected static final String EUR = 'EUR'
    protected static final String USD = 'USD'

    protected LobbyFacade lobbyFacade
    protected UserTranslationService userTranslator
    protected CarTranslationService carService
    protected EventPublisher eventPublisher

    def setup() {
        userTranslator = Stub()
        carService = Stub()
        eventPublisher = Mock()
        lobbyFacade = new LobbyConfiguration().lobbyFacade(userTranslator, carService, eventPublisher)
    }

    protected loggedDriver(final UUID driverId, final String driverLogin) {
        def driver = new Participant(
                participantId: driverId,
                displayName: driverLogin,
                participantType: ParticipantType.CAR_DRIVER,
                travelCurrency: PLN)

        userTranslator.getCurrentLoggedDriver() >> new Driver(driverId)
        userTranslator.getAsDriver(driverId) >> driver
        userTranslator.getCurrentUserAsDriver() >> driver
    }

    protected loggedDriver(final UUID driverId, final String driverLogin, final String defaultCurrency) {
        def driver = new Participant(
                participantId: driverId,
                displayName: driverLogin,
                participantType: ParticipantType.CAR_DRIVER,
                travelCurrency: defaultCurrency)

        userTranslator.getCurrentLoggedDriver() >> new Driver(driverId)
        userTranslator.getAsDriver(driverId) >> driver
        userTranslator.getCurrentUserAsDriver() >> driver
    }

    protected driverExists(final UUID driverId, final String driverLogin) {
        userTranslator.getAsDriver(driverId) >> new Participant(
                participantId: driverId,
                displayName: driverLogin,
                participantType: ParticipantType.CAR_DRIVER,
                travelCurrency: PLN)
    }

    protected passengerExists(final UUID participantId, final String displayName, final String travelCurrency) {
        userTranslator.getPassenger(participantId) >> new Participant(participantId, displayName, ParticipantType.PASSENGER, Currency.valueOf(travelCurrency))
    }

    protected def carExists(final UUID carId, final UUID carOwner, final int seats, final int occupiedSeats) {
        carService.getCar(carId) >> new Car(CarId.of(carId), carOwner, seats, occupiedSeats)
    }

}
