package com.splitoil.travel.travelflow.domain.model

import com.splitoil.UnitTest
import com.splitoil.infrastructure.json.JacksonAdapter
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class RouteSerializationTest extends Specification {

    public static final String EMPTY = '{"waypoints":[],"travelId":null}'
    public static final String ALL_WAYPOINTS = '{"waypoints":[{"id":"1435ba24-fd5d-4d9a-b19f-6224f0b2e210","location":{"lon":10.0,"lat":15.0},"waypointType":"BEGINNING_PLACE","historical":false},{"id":"1b6a488f-237a-4bad-9f84-c2200aa6f9cf","location":{"lon":10.0,"lat":15.0},"waypointType":"DESTINATION_PLACE","historical":false}],"travelId":null}'
    public static final String WAYPOINT = '{"id":"1435ba24-fd5d-4d9a-b19f-6224f0b2e210","location":{"lon":10.0,"lat":15.0},"waypointType":"BEGINNING_PLACE","historical":false}'
    private static final GeoPoint GEO_POINT = GeoPoint.of(10, 15)
    private static final UUID BEGINNING_UUID = UUID.fromString('1435ba24-fd5d-4d9a-b19f-6224f0b2e210')
    private static final UUID DESTINATION_UUID = UUID.fromString('1b6a488f-237a-4bad-9f84-c2200aa6f9cf')


    def 'serialize empty'() {
        when:
            def route = new Route()
            def json = JacksonAdapter.getInstance().toJson(route)

        then:
            json == EMPTY
    }

    def 'deserialize empty'() {
        when:
            def empty = new Route()
            def route = JacksonAdapter.getInstance().jsonDecode(EMPTY, Route)

        then:
            empty == route
    }

    def 'serialize route'() {
        when:
            def route = new Route()
            def waypoint = new Waypoint(BEGINNING_UUID, GEO_POINT, Waypoint.WaypointType.BEGINNING_PLACE, false)
            def waypoint2 = new Waypoint(DESTINATION_UUID, GEO_POINT, Waypoint.WaypointType.DESTINATION_PLACE, false)
            route.addWaypoint(waypoint)
            route.addWaypoint(waypoint2)
            def json = JacksonAdapter.getInstance().toJson(route)

        then:
            json == ALL_WAYPOINTS
    }

    def 'deserialize route'() {
        when:
            def route = new Route()
            def waypoint = new Waypoint(BEGINNING_UUID, GEO_POINT, Waypoint.WaypointType.BEGINNING_PLACE, false)
            def waypoint2 = new Waypoint(DESTINATION_UUID, GEO_POINT, Waypoint.WaypointType.DESTINATION_PLACE, false)
            route.addWaypoint(waypoint)
            route.addWaypoint(waypoint2)
            def fromJson = JacksonAdapter.getInstance().jsonDecode(ALL_WAYPOINTS, Route)

        then:
            route == fromJson
    }

    def 'serialize waypoint'() {
        when:
            def waypoint = new Waypoint(BEGINNING_UUID, GEO_POINT, Waypoint.WaypointType.BEGINNING_PLACE, false)
            def json = JacksonAdapter.getInstance().toJson(waypoint)

        then:
            json == WAYPOINT
    }

    def 'deserialize waypoint'() {
        when:
            def waypoint = new Waypoint(BEGINNING_UUID, GEO_POINT, Waypoint.WaypointType.BEGINNING_PLACE, false)
            def fromJson = JacksonAdapter.getInstance().jsonDecode(WAYPOINT, Waypoint)

        then:
            waypoint == fromJson
    }

}
