package com.splitoil.travel.travelflow.domain.model;

import groovy.util.logging.Slf4j;
import lombok.NonNull;
import lombok.Value;

import java.util.EnumSet;
import java.util.List;

import static com.splitoil.travel.travelflow.domain.model.Waypoint.WaypointType.BEGINNING_PLACE;
import static com.splitoil.travel.travelflow.domain.model.Waypoint.WaypointType.DESTINATION_PLACE;

@Slf4j
@FunctionalInterface
interface WaypointAddingRulesPolicy {

    WaypointAddingCheckResult canAddWaypoint(List<Waypoint> waypoints, Waypoint waypoint);

    EnumSet<Waypoint.WaypointType> ONLY_ONCE_RULES = EnumSet.of(BEGINNING_PLACE, DESTINATION_PLACE);

    WaypointAddingRulesPolicy addOnlyOnceWaypointPolicy = (waypoints, waypoint) -> {
        if (!ONLY_ONCE_RULES.contains(waypoint.getWaypointType())) {
            return WaypointAddingCheckResult.isFollowingTheRule();
        }

        if (waypoints.stream().anyMatch(w -> w.is(waypoint.getWaypointType()))) {
            return WaypointAddingCheckResult.isNotFollowingTheRule(String.format("Only one waypoint of type %s can be present", waypoint.getWaypointType()));
        }

        return WaypointAddingCheckResult.isFollowingTheRule();
    };

    static List<WaypointAddingRulesPolicy> allCurrentPolicies() {
        return List.of(addOnlyOnceWaypointPolicy);
    }

    @Value
    class WaypointAddingCheckResult {
        boolean follows;
        String message;

        static WaypointAddingCheckResult isFollowingTheRule() {
            return new WaypointAddingCheckResult(true, "");
        }

        static WaypointAddingCheckResult isNotFollowingTheRule(final @NonNull String reason) {
            return new WaypointAddingCheckResult(false, reason);
        }

        boolean isError() {
            return !follows;
        }
    }
}
