package com.splitoil.travel.travelflow.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.List;

import static com.splitoil.travel.travelflow.domain.model.Waypoint.WaypointType.BEGINNING_PLACE;
import static com.splitoil.travel.travelflow.domain.model.Waypoint.WaypointType.DESTINATION_PLACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Waypoints {
    static final EnumSet<Waypoint.WaypointType> ONLY_ONCE_WAYPOINTS = EnumSet.of(BEGINNING_PLACE, DESTINATION_PLACE);

    static int getIndexOfFirstActiveWaypoint(final List<Waypoint> waypoints) {
        for (final Waypoint waypoint : waypoints) {
            if (waypoint.isActive()) {
                return waypoints.indexOf(waypoint);
            }
        }

        //TODO: wszystkie historical
        throw new IllegalStateException("Zrob cos");
    }
}
