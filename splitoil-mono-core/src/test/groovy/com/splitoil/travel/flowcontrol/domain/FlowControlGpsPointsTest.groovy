package com.splitoil.travel.flowcontrol.domain


import com.splitoil.travel.flowcontrol.domain.event.WaypointReached
import com.splitoil.travel.flowcontrol.web.dto.AddGpsPointCommand
import com.splitoil.travel.flowcontrol.web.dto.AddWaypointReachedGpsPointCommand
import com.splitoil.travel.flowcontrol.web.dto.GeoPointDto

class FlowControlGpsPointsTest extends FlowControlTest {

    def 'Should add next gps point'() {
        setup:
            def flowControlId = withFlowControlOfCarAndTravel(CAR_ID, TRAVEL_ID)

        when:
            flowControlFacade.addGpsPoint(new AddGpsPointCommand(flowControlId: flowControlId, geoPoint: new GeoPointDto(lon: 1, lat: 1)))
            flowControlFacade.addGpsPoint(new AddGpsPointCommand(flowControlId: flowControlId, geoPoint: new GeoPointDto(lon: 1, lat: 2)))
            flowControlFacade.addGpsPoint(new AddGpsPointCommand(flowControlId: flowControlId, geoPoint: new GeoPointDto(lon: 2, lat: 2)))

        then:
            def points = flowControlQuery.getGpsPointsList(flowControlId)
            points[0].geoPoint.lon == 1
            points[0].geoPoint.lat == 1

            points[1].geoPoint.lon == 1
            points[1].geoPoint.lat == 2

            points[2].geoPoint.lon == 2
            points[2].geoPoint.lat == 2
    }

    def 'Should add next gps point with waypoint'() {
        setup:
            def flowControlId = withFlowControlOfCarAndTravel(CAR_ID, TRAVEL_ID)

        when:
            flowControlFacade.addGpsPoint(new AddGpsPointCommand(flowControlId: flowControlId, geoPoint: new GeoPointDto(lon: 1, lat: 1)))
            flowControlFacade.addWaypointGpsPoint(new AddWaypointReachedGpsPointCommand(
                    flowControlId: flowControlId,
                    geoPoint: new GeoPointDto(lon: 1, lat: 2),
                    waypointId: WAYPOINT_ID,
                    waypointType: 'CHECKPOINT'))
            flowControlFacade.addGpsPoint(new AddGpsPointCommand(flowControlId: flowControlId, geoPoint: new GeoPointDto(lon: 2, lat: 2)))

        then:
            def points = flowControlQuery.getGpsPointsList(flowControlId)
            points[0].geoPoint.lon == 1
            points[0].geoPoint.lat == 1

            points[1].geoPoint.lon == 1
            points[1].geoPoint.lat == 2
            points[1].associatedWaypointId == WAYPOINT_ID
            points[1].waypointType == 'CHECKPOINT'

            points[2].geoPoint.lon == 2
            points[2].geoPoint.lat == 2
    }

    def waypointReachedEvent = null

    def 'Should inform about waypoint reached'() {
        setup:
            def flowControlId = withFlowControlOfCarAndTravel(CAR_ID, TRAVEL_ID)

        when:
            flowControlFacade.addWaypointGpsPoint(new AddWaypointReachedGpsPointCommand(
                    flowControlId: flowControlId,
                    geoPoint: new GeoPointDto(lon: 1, lat: 2),
                    waypointId: WAYPOINT_ID,
                    waypointType: 'CHECKPOINT'))

        then: 'Event published'
            1 * eventPublisher.publish(_ as WaypointReached) >> { waypointReachedEvent = it[0] }
            waypointReachedEvent.carId == CAR_ID
            waypointReachedEvent.waypointId == WAYPOINT_ID
    }

    def 'Should ignore same waypoint reached events'() {
        setup:
            def flowControlId = withFlowControlOfCarAndTravel(CAR_ID, TRAVEL_ID)

            def addWaypointReachedGpsPointCommand = new AddWaypointReachedGpsPointCommand(
                    flowControlId: flowControlId,
                    geoPoint: new GeoPointDto(lon: 1, lat: 2),
                    waypointId: WAYPOINT_ID,
                    waypointType: 'CHECKPOINT')
        when:
            flowControlFacade.addWaypointGpsPoint(addWaypointReachedGpsPointCommand)
            flowControlFacade.addWaypointGpsPoint(addWaypointReachedGpsPointCommand)
            flowControlFacade.addWaypointGpsPoint(addWaypointReachedGpsPointCommand)

        then: 'Event deduplicated'
            flowControlQuery.getGpsPointsList(flowControlId).size() == 1
            1 * eventPublisher.publish(_ as WaypointReached)
    }
}
