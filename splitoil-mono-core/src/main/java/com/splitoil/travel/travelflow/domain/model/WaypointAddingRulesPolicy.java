package com.splitoil.travel.travelflow.domain.model;

import groovy.util.logging.Slf4j;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Slf4j
@FunctionalInterface
interface WaypointAddingRulesPolicy {

    WaypointAddingCheckResult canAddWaypoint(List<Waypoint> waypoints, Waypoint waypoint);

    WaypointAddingRulesPolicy addOnlyOnceWaypointPolicy = (waypoints, waypoint) -> {
        if (!Waypoints.ONLY_ONCE_WAYPOINTS.contains(waypoint.getWaypointType())) {
            return WaypointAddingCheckResult.isFollowingTheRule();
        }

        if (waypoints.stream().anyMatch(w -> w.is(waypoint.getWaypointType()))) {
            return WaypointAddingCheckResult.isNotFollowingTheRule(String.format("Only one waypoint of type %s can be present", waypoint.getWaypointType()));
        }

        return WaypointAddingCheckResult.isFollowingTheRule();
    };

    WaypointAddingRulesPolicy addAfterBeginningAndDestinationIsSetPolicy = (waypoints, waypoint) -> {
        if (Waypoints.ONLY_ONCE_WAYPOINTS.contains(waypoint.getWaypointType())) {
            return WaypointAddingCheckResult.isFollowingTheRule();
        }

        final boolean beginningSet = waypoints.stream().anyMatch(Waypoint::isBeginning);
        final boolean destinationSet = waypoints.stream().anyMatch(Waypoint::isDestination);

        return beginningSet && destinationSet ?
            WaypointAddingCheckResult.isFollowingTheRule() :
            WaypointAddingCheckResult.isNotFollowingTheRule(
                String.format("Can't add %s waypoint before adding beginning and destination waypoint first", waypoint.getWaypointType().name()));
    };

    static List<WaypointAddingRulesPolicy> allCurrentPolicies() {
        return List.of(addOnlyOnceWaypointPolicy, addAfterBeginningAndDestinationIsSetPolicy);
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

