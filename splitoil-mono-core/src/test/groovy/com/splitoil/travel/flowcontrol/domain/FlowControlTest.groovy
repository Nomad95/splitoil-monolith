package com.splitoil.travel.flowcontrol.domain

import com.splitoil.UnitTest
import com.splitoil.shared.event.EventPublisher
import com.splitoil.travel.flowcontrol.application.FlowControlFacade
import com.splitoil.travel.flowcontrol.application.FlowControlQuery
import com.splitoil.travel.flowcontrol.application.command.CreateFlowControlCommand
import com.splitoil.travel.flowcontrol.infrastructure.database.InMemoryFlowControlRepository
import com.splitoil.travel.lobby.infrastructure.FlowControlConfiguration
import com.splitoil.travel.travel.web.dto.GeoPointDto
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class FlowControlTest extends Specification {

    protected static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    protected static final UUID SECOND_DRIVER_ID = UUID.fromString('25151ea3-239d-453f-bc49-d0345d9210bf')
    protected static final UUID CAR_ID = UUID.fromString('1ad11d82-5f49-4761-abc3-f8cd9b9bd594')
    protected static final UUID SECOND_CAR_ID = UUID.fromString('29c9c416-cad6-4845-97a0-3d2b1bf496e9')
    protected static final UUID PASSENGER_1_ID = UUID.fromString('1a26cbe6-1c70-42a2-8734-6b861702672d')
    protected static final UUID PASSENGER_2_ID = UUID.fromString('46e853a4-bb2e-43ab-8e88-80807468954d')
    protected static final UUID WAYPOINT_ID = UUID.fromString('4dbd718c-f9b1-4c65-9b3c-0af8e5eaf5d1')
    protected static final UUID LOBBY_ID = UUID.fromString('148091b1-c0f0-4a3e-9b0d-569e05cfcd0f')
    protected static final UUID TRAVEL_ID = UUID.fromString('799c470e-b0d6-4df9-8bbe-195533515826')

    protected static final String LOBBY_NAME = "Some lobby name"

    protected static final GeoPointDto BEGINNING_LOCATION = GeoPointDto.of(0,0)
    protected static final GeoPointDto SOME_LOCATION = GeoPointDto.of(50,50)
    protected static final GeoPointDto DESTINATION_LOCATION = GeoPointDto.of(1000,1000)

    protected FlowControlFacade flowControlFacade
    protected FlowControlQuery flowControlQuery
    protected EventPublisher eventPublisher

    def setup() {
        eventPublisher = Mock()
        def repository = new InMemoryFlowControlRepository()
        flowControlQuery = new FlowControlQuery(repository)
        flowControlFacade = new FlowControlConfiguration().flowControl(eventPublisher, repository)
    }

    protected UUID withFlowControlOfCarAndTravel(carId, travelId) {
        def command = new CreateFlowControlCommand(travelId, carId, travelId)
        return flowControlFacade.createFlowControl(command).getId()
    }


}
