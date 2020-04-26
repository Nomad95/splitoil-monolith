package com.splitoil.travel.travelflow.domain

import com.splitoil.shared.event.EventPublisher
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested
import com.splitoil.travel.lobby.web.dto.ForTravelCreationLobbyDto
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto
import com.splitoil.travel.travelflow.application.TravelFlowFacade
import com.splitoil.travel.travelflow.infrastructure.TravelConfiguration
import com.splitoil.travel.travelflow.web.dto.GeoPointDto
import spock.lang.Specification

class TravelTest extends Specification {
    protected static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    protected static final UUID SECOND_DRIVER_ID = UUID.fromString('25151ea3-239d-453f-bc49-d0345d9210bf')
    protected static final UUID CAR_ID = UUID.fromString('1ad11d82-5f49-4761-abc3-f8cd9b9bd594')
    protected static final UUID SECOND_CAR_ID = UUID.fromString('29c9c416-cad6-4845-97a0-3d2b1bf496e9')
    protected static final UUID PASSENGER_1_ID = UUID.fromString('1a26cbe6-1c70-42a2-8734-6b861702672d')
    protected static final UUID PASSENGER_2_ID = UUID.fromString('46e853a4-bb2e-43ab-8e88-80807468954d')
    protected static final UUID PASSENGER_3_ID = UUID.fromString('4dbd718c-f9b1-4c65-9b3c-0af8e5eaf5d1')
    protected static final UUID LOBBY_ID = UUID.fromString('148091b1-c0f0-4a3e-9b0d-569e05cfcd0f')

    protected static final GeoPointDto BEGINNING_LOCATION = GeoPointDto.of(0,0)
    protected static final GeoPointDto DESTINATION_LOCATION = GeoPointDto.of(1000,1000)

    protected TravelFlowFacade travelFlowFacade
    protected EventPublisher eventPublisher

    def setup() {
        eventPublisher = Mock()
        travelFlowFacade = new TravelConfiguration().travelFlowFacade(eventPublisher)
    }

    protected travelWithTwoCarsAndThreeParticipants() {
        def lobbyDto = ForTravelCreationLobbyDto.builder()
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(DRIVER_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(SECOND_DRIVER_ID).assignedCar(SECOND_CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_1_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_2_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_3_ID).assignedCar(SECOND_CAR_ID).build())
                .build()
        def createTravelCommand = new TravelCreationRequested(LOBBY_ID, lobbyDto)

        return travelFlowFacade.createNewTravel(createTravelCommand)
    }

//    protected loggedDriver(final UUID driverId, final String driverLogin) {
//        def driver = new Participant(
//                participantId: driverId,
//                displayName: driverLogin,
//                participantType: ParticipantType.CAR_DRIVER,
//                travelCurrency: PLN)
//
//        userTranslator.getCurrentLoggedDriver() >> new Driver(driverId)
//        userTranslator.getAsDriver(driverId) >> driver
//        userTranslator.getCurrentUserAsDriver() >> driver
//    }
//
//    protected loggedDriver(final UUID driverId, final String driverLogin, final String defaultCurrency) {
//        def driver = new Participant(
//                participantId: driverId,
//                displayName: driverLogin,
//                participantType: ParticipantType.CAR_DRIVER,
//                travelCurrency: defaultCurrency)
//
//        userTranslator.getCurrentLoggedDriver() >> new Driver(driverId)
//        userTranslator.getAsDriver(driverId) >> driver
//        userTranslator.getCurrentUserAsDriver() >> driver
//    }
//
//    protected driverExists(final UUID driverId, final String driverLogin) {
//        userTranslator.getAsDriver(driverId) >> new Participant(
//                participantId: driverId,
//                displayName: driverLogin,
//                participantType: ParticipantType.CAR_DRIVER,
//                travelCurrency: PLN)
//    }
//
//    protected passengerExists(final UUID participantId, final String displayName, final String travelCurrency) {
//        userTranslator.getPassenger(participantId) >> new Participant(participantId, displayName, ParticipantType.PASSENGER, Currency.valueOf(travelCurrency))
//    }
//
//    protected def carExists(final UUID carId, final UUID carOwner, final int seats, final int occupiedSeats) {
//        carService.getCar(carId) >> new Car(CarId.of(carId), carOwner, seats, occupiedSeats)
//    }

}
