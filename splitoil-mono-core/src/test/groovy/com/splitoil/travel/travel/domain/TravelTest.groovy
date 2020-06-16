package com.splitoil.travel.travel.domain

import com.splitoil.shared.event.EventPublisher
import com.splitoil.travel.lobby.application.LobbyQuery
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested
import com.splitoil.travel.lobby.web.dto.ForTravelCreationLobbyDto
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto
import com.splitoil.travel.travel.application.TravelFlowFacade
import com.splitoil.travel.travel.infrastructure.TravelConfiguration
import com.splitoil.travel.travel.web.dto.*
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
    protected static final UUID TRAVEL_ID = UUID.fromString('1511e033-5db3-4e4d-a498-7e7f32cfc063')

    protected static final String LOBBY_NAME = "Some lobby name"

    protected static final GeoPointDto BEGINNING_LOCATION = GeoPointDto.of(0,0)
    protected static final GeoPointDto SOME_LOCATION = GeoPointDto.of(50,50)
    protected static final GeoPointDto DESTINATION_LOCATION = GeoPointDto.of(1000,1000)

    protected TravelFlowFacade travelFlowFacade
    protected EventPublisher eventPublisher
    protected LobbyQuery lobbyQuery

    def setup() {
        eventPublisher = Mock()
        lobbyQuery = Mock()
        travelFlowFacade = new TravelConfiguration().travelFlowFacade(eventPublisher, lobbyQuery)
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

    protected travelWithTwoCarsAndThreeParticipantsAndDestinationSelected() {
        def lobbyDto = ForTravelCreationLobbyDto.builder()
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(DRIVER_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(SECOND_DRIVER_ID).assignedCar(SECOND_CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_1_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_2_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_3_ID).assignedCar(SECOND_CAR_ID).build())
                .build()
        def createTravelCommand = new TravelCreationRequested(LOBBY_ID, lobbyDto)

        def travel = travelFlowFacade.createNewTravel(createTravelCommand)
        travelFlowFacade.selectTravelBeginning(new SelectTravelBeginningCommand(travel.getTravelId(), BEGINNING_LOCATION))
        travelFlowFacade.selectTravelDestination(new SelectTravelDestinationCommand(travel.getTravelId(), DESTINATION_LOCATION))
        return travel
    }

    protected confirmedPlan() {
        def lobbyDto = ForTravelCreationLobbyDto.builder()
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(DRIVER_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(SECOND_DRIVER_ID).assignedCar(SECOND_CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_1_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_2_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_3_ID).assignedCar(SECOND_CAR_ID).build())
                .build()
        def createTravelCommand = new TravelCreationRequested(LOBBY_ID, lobbyDto)

        def travel = travelFlowFacade.createNewTravel(createTravelCommand)
        travelFlowFacade.selectTravelBeginning(new SelectTravelBeginningCommand(travel.getTravelId(), BEGINNING_LOCATION))
        travelFlowFacade.selectTravelDestination(new SelectTravelDestinationCommand(travel.getTravelId(), DESTINATION_LOCATION))

        travelFlowFacade.confirmPlan(ConfirmTravelPlanCommand.of(travel.travelId))

        return travelFlowFacade.getTravel(travel.travelId)
    }

    protected confirmedPlanWithInitialStateSet() {
        def lobbyDto = ForTravelCreationLobbyDto.builder()
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(DRIVER_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(SECOND_DRIVER_ID).assignedCar(SECOND_CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_1_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_2_ID).assignedCar(CAR_ID).build())
                .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_3_ID).assignedCar(SECOND_CAR_ID).build())
                .build()
        def createTravelCommand = new TravelCreationRequested(LOBBY_ID, lobbyDto)

        def travel = travelFlowFacade.createNewTravel(createTravelCommand)
        travelFlowFacade.selectTravelBeginning(new SelectTravelBeginningCommand(travel.getTravelId(), BEGINNING_LOCATION))
        travelFlowFacade.selectTravelDestination(new SelectTravelDestinationCommand(travel.getTravelId(), DESTINATION_LOCATION))

        travelFlowFacade.confirmPlan(ConfirmTravelPlanCommand.of(travel.travelId))

        def setState1 = new SetCarInitialStateCommand( travel.travelId, CAR_ID, BigDecimal.TEN, 1234)
        def setState2 = new SetCarInitialStateCommand(travel.travelId, SECOND_CAR_ID, BigDecimal.TEN, 1234)
        travelFlowFacade.setCarInitialState(setState1)
        travelFlowFacade.setCarInitialState(setState2)

        return travelFlowFacade.getTravel(travel.travelId)
    }

    protected allPassengersAndCarsExistsInLobby() {
        lobbyQuery.participantExistsInLobby(_ as UUID, _ as UUID) >> true
        lobbyQuery.carsExistInLobby(_ as List<UUID>, _ as UUID) >> true
        lobbyQuery.carExistInLobby(_ as UUID, _ as UUID) >> true
        lobbyQuery.getLobbyCarsIds(_ as UUID) >> [CAR_ID, SECOND_CAR_ID]
    }

    protected passengersAreInTheLobbyButNotCar() {
        lobbyQuery.participantExistsInLobby(_ as UUID, _ as UUID) >> true
        lobbyQuery.carsExistInLobby(_ as List<UUID>, _ as UUID) >> false
        lobbyQuery.carExistInLobby(_ as UUID, _ as UUID) >> false
        lobbyQuery.getLobbyCarsIds(_ as UUID) >> [CAR_ID, SECOND_CAR_ID]
    }

    protected allCarsArePresentInLobbyButNotPassengers() {
        lobbyQuery.participantExistsInLobby(_ as UUID, _ as UUID) >> false
        lobbyQuery.carsExistInLobby(_ as List<UUID>, _ as UUID) >> true
        lobbyQuery.carExistInLobby(_ as UUID, _ as UUID) >> true
        lobbyQuery.getLobbyCarsIds(_ as UUID) >> [CAR_ID, SECOND_CAR_ID]
    }

}
