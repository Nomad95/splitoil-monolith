package com.splitoil.travel.travel.domain

import com.splitoil.UnitTest
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested
import com.splitoil.travel.lobby.web.dto.ForTravelCreationLobbyDto
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto
import com.splitoil.travel.travel.domain.event.TravelCreated
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a lobby creator i want to start defining travel route
""")
@See('resources/cucumber/defining_travel.feature')
class TravelCreationTest extends TravelTest {

    def "Driver starts defining travel route"() {
        given: 'Start defining travel route asked'
            def lobbyDto = ForTravelCreationLobbyDto.builder()
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(DRIVER_ID).assignedCar(CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(SECOND_DRIVER_ID).assignedCar(SECOND_CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_1_ID).assignedCar(CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_2_ID).assignedCar(CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_3_ID).assignedCar(SECOND_CAR_ID).build())
                    .build()
            def createTravelCommand = new TravelCreationRequested(LOBBY_ID, lobbyDto)

        when: 'Create travel'
            travelFlowFacade.createNewTravel(createTravelCommand)

        then: 'Event published'
            1 * eventPublisher.publish(_ as TravelCreated)
    }

    def "Created travel is always in IN_CONFIGURATION state"() {
        given: 'Start defining travel route asked'
            def lobbyDto = ForTravelCreationLobbyDto.builder()
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(DRIVER_ID).assignedCar(CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(SECOND_DRIVER_ID).assignedCar(SECOND_CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_1_ID).assignedCar(CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_2_ID).assignedCar(CAR_ID).build())
                    .participant(LobbyParticipantForTravelPlanDto.builder().userId(PASSENGER_3_ID).assignedCar(SECOND_CAR_ID).build())
                    .build()
            def createTravelCommand = new TravelCreationRequested(LOBBY_ID, lobbyDto)

        when: 'Create travel'
            def travel = travelFlowFacade.createNewTravel(createTravelCommand)

        then: 'travel is in IN_CONFIGURATION state'
            travel.travelStatus == "IN_CONFIGURATION"
    }
}
