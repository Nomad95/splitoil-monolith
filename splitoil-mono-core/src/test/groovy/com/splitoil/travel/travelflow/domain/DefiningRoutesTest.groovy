package com.splitoil.travel.travelflow.domain

import com.splitoil.UnitTest
import com.splitoil.travel.travelflow.domain.event.TravelBeginningPlaceSelected
import com.splitoil.travel.travelflow.domain.event.TravelDestinationPlaceSelected
import com.splitoil.travel.travelflow.web.dto.SelectTravelBeginningCommand
import com.splitoil.travel.travelflow.web.dto.SelectTravelDestinationCommand
import org.junit.experimental.categories.Category
import spock.lang.Narrative
import spock.lang.See

@Category(UnitTest)
@Narrative("""
As a Lobby creator i want to define the travel route to show it to the participants
""")
@See('resources/cucumber/defining_travel.feature')
class DefiningRoutesTest extends TravelTest {

    def "Lobby creator can select travel beginning"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipants()

        when: 'Lobby creator selects travel beginning'
            def command = SelectTravelBeginningCommand.of(travel.travelId, BEGINNING_LOCATION)
            travelFlowFacade.selectTravelBeginning(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Travel beginning set'
            route.getWaypoints().size() == 1
            route.getWaypoints().get(0).getWaypointType() == "BEGINNING_PLACE"

            1 * eventPublisher.publish(_ as TravelBeginningPlaceSelected)
    }

    def "Lobby creator can select travel destination"() {
        setup: 'New travel'
            def travel = travelWithTwoCarsAndThreeParticipants()

        when: 'Lobby creator selects travel destination'
            def command = SelectTravelDestinationCommand.of(travel.travelId, DESTINATION_LOCATION)
            travelFlowFacade.selectTravelDestination(command)
            def route = travelFlowFacade.getRoute(travel.travelId)

        then: 'Travel destination set'
            route.getWaypoints().size() == 1
            route.getWaypoints().get(0).getWaypointType() == "DESTINATION_PLACE"

            1 * eventPublisher.publish(_ as TravelDestinationPlaceSelected)
    }
}
