package com.splitoil.travel.travel.domain.model

import com.splitoil.UnitTest
import com.splitoil.travel.travel.domain.TravelTest
import com.splitoil.travel.travel.web.dto.waypoint.*
import org.junit.experimental.categories.Category

@Category(UnitTest)
class WaypointsAdditionalInfoTest extends TravelTest {

    private static final GeoPoint LOCATION = GeoPoint.of(0, 0)

    private static final BoardingPlaceInfoDto BOARDING_PLACE_INFO = new BoardingPlaceInfoDto(PASSENGER_1_ID, CAR_ID)
    private static final ExitPlaceInfoDto EXIT_PLACE_INFO = new ExitPlaceInfoDto(PASSENGER_1_ID, CAR_ID)
    private static final RefuelPlaceInfoDto CAR_REFUEL_PLACE_INFO = new RefuelPlaceInfoDto(CAR_ID)
    private static final ReseatPlaceInfoDto RESEAT_PLACE_INFO = new ReseatPlaceInfoDto(PASSENGER_1_ID, CAR_ID, SECOND_CAR_ID)
    private static final EmptyInfoDto EMPTY_INFO = new EmptyInfoDto()


    def 'Should add a payload to passenger boarding place'() {
        given: 'Route'
            def route = travelPlanWithBeginningAndEndPlace()

        when: 'Add boarding waypoint with info'
            route.addWaypoint(Waypoint.participantBoardingPlace(LOCATION, new PassengerBoardingPlaceInfo(PASSENGER_1_ID, CAR_ID)))

        then: 'Additional info is present in dto'
            route.toRouteDto().getWaypoints().get(1).getAdditionalInfo() == BOARDING_PLACE_INFO
    }

    def 'Should add a payload to passenger exit place'() {
        given: 'Route'
            def route = travelPlanWithBeginningAndEndPlace()

        when: 'Add exit waypoint with info'
            route.addWaypoint(Waypoint.passengerExitPlace(LOCATION, new PassengerExitPlaceInfo(PASSENGER_1_ID, CAR_ID)))

        then: 'Additional info is present in dto'
            route.toRouteDto().getWaypoints().get(1).getAdditionalInfo() == EXIT_PLACE_INFO
    }

    def 'Should add a car refuel place'() {
        given: 'Route'
            def route = travelPlanWithBeginningAndEndPlace()

        when: 'Add car refuel waypoint with info'
            route.addWaypoint(Waypoint.refuelPlace(LOCATION, new CarRefuelPlaceInfo(CAR_ID)))

        then: 'Additional info is present in dto'
            route.toRouteDto().getWaypoints().get(1).getAdditionalInfo() == CAR_REFUEL_PLACE_INFO
    }

    def 'Should add a reseat place'() {
        given: 'Route'
            def route = travelPlanWithBeginningAndEndPlace()

        when: 'Add reseat waypoint with info'
            route.addWaypoint(Waypoint.reseatPlace(LOCATION, new PassengerReseatPlaceInfo(PASSENGER_1_ID, CAR_ID, SECOND_CAR_ID)))

        then: 'Additional info is present in dto'
            route.toRouteDto().getWaypoints().get(1).getAdditionalInfo() == RESEAT_PLACE_INFO
    }

    def 'Checkpoints adds no additional info to waypoint'() {
        given: 'Route'
            def route = travelPlanWithBeginningAndEndPlace()

        when: 'Add checkpoint waypoint with info'
            route.addWaypoint(Waypoint.checkpoint(LOCATION))

        then: 'Additional info is present in dto'
            route.toRouteDto().getWaypoints().get(1).getAdditionalInfo() == EMPTY_INFO
    }

    def travelPlanWithBeginningAndEndPlace() {
        def travelPlan = new Route()
        travelPlan.addWaypoint(Waypoint.beginningPlace(LOCATION))
        travelPlan.addWaypoint(Waypoint.destinationPlace(LOCATION))

        return travelPlan
    }

}
