package com.splitoil.travel.travel.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.List;

import static com.splitoil.travel.travel.domain.model.Waypoint.WaypointType.BEGINNING_PLACE;
import static com.splitoil.travel.travel.domain.model.Waypoint.WaypointType.DESTINATION_PLACE;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Waypoints {
    static final EnumSet<Waypoint.WaypointType> ONLY_ONCE_WAYPOINTS = EnumSet.of(BEGINNING_PLACE, DESTINATION_PLACE);

    static WaypointIndex getIndexOfFirstActiveWaypoint(final List<Waypoint> waypoints) {
        for (final Waypoint waypoint : waypoints) {
            if (waypoint.isActive()) {
                return WaypointIndex.of(waypoints.indexOf(waypoint));
            }
        }

        log.info("All waypoints are historical");
        return WaypointIndex.ALL_HISTORICAL;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
    static class WaypointIndex {
        static final WaypointIndex ALL_HISTORICAL = WaypointIndex.of(-1);

        @Getter
        private final int index;
    }
}
