package com.splitoil.travel.lobby.domain

import com.splitoil.travel.lobby.application.CarTranslationService
import com.splitoil.travel.lobby.application.LobbyService
import com.splitoil.travel.lobby.application.UserTranslationService
import com.splitoil.travel.lobby.domain.model.*
import com.splitoil.travel.lobby.infrastructure.LobbyConfiguration
import spock.lang.Specification

class LobbyTest extends Specification {
    protected static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    protected static final UUID CAR_ID = UUID.fromString('1ad11d82-5f49-4761-abc3-f8cd9b9bd594')
    protected static final UUID PASSENGER_1_ID = UUID.fromString('1a26cbe6-1c70-42a2-8734-6b861702672d')

    protected static final String LOBBY_NAME = "Some lobby name"
    protected static final String PASSENGER_NAME = 'Wojtaszko'
    protected static final String DRIVER_LOGIN = 'Wojtapsiux'

    protected static final String PLN = 'PLN'
    protected static final String EUR = 'EUR'
    protected static final String USD = 'USD'

    protected LobbyService lobbyService
    protected UserTranslationService userTranslator
    protected CarTranslationService carService

    def setup() {
        userTranslator = Stub()
        carService = Stub()
        lobbyService = new LobbyConfiguration().lobbyService(userTranslator, carService)
    }

    protected loggedDriver(final UUID driverId, final String driverLogin) {
        userTranslator.getCurrentLoggedDriver() >> new Driver(driverId)
        userTranslator.getCurrentUserAsDriver() >> new Participant(
                participantId: driverId,
                displayName: driverLogin,
                participantType: ParticipantType.CAR_DRIVER)
    }

    protected passenger(final UUID participantId, final String displayName) {
        userTranslator.getPassenger(_) >> new Participant(participantId, displayName, ParticipantType.PASSENGER)
    }

    protected def addCar(final UUID carId, final UUID carOwner, final int seats, final int occupiedSeats) {
        carService.getCar(carId) >> new Car(CarId.of(carId), carOwner, seats, occupiedSeats)
    }

}
