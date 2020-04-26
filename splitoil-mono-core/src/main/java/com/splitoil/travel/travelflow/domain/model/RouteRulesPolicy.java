package com.splitoil.travel.travelflow.domain.model;

import groovy.util.logging.Slf4j;
import lombok.NonNull;
import lombok.Value;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.splitoil.travel.travelflow.domain.model.Waypoint.WaypointType.BEGINNING_PLACE;
import static com.splitoil.travel.travelflow.domain.model.Waypoint.WaypointType.DESTINATION_PLACE;

@Slf4j
@FunctionalInterface
interface RouteRulesPolicy {

    RouteRuleCheckResult followsTheRule(List<Waypoint> waypoints);

    EnumSet<Waypoint.WaypointType> ONLY_ONCE_RULES = EnumSet.of(BEGINNING_PLACE, DESTINATION_PLACE);

    RouteRulesPolicy onlyOncePolicy = waypoints -> {
        for (final Waypoint.WaypointType waypointType : ONLY_ONCE_RULES) {
            if (waypoints.stream().filter(waypoint -> waypoint.is(waypointType)).count() > 1) {
                return RouteRuleCheckResult.isNotFollowingTheRule(String.format("Waypoint of type %s occurs more than once", waypointType.name()));
            }
        }

        return RouteRuleCheckResult.isFollowingTheRule();
    };

    RouteRulesPolicy atStartOnlyPolicy = waypoints -> {
        if (waypoints.size() < 2) {
            return RouteRuleCheckResult.isFollowingTheRule();
        }

        if (!waypoints.get(0).isBeginning()) {
            return RouteRuleCheckResult.isNotFollowingTheRule("First waypoint is not a beginning place!");
        }

        return RouteRuleCheckResult.isFollowingTheRule();
    };

    RouteRulesPolicy atEndOnlyPolicy = waypoints -> {
        if (waypoints.size() < 2) {
            return RouteRuleCheckResult.isFollowingTheRule();
        }

        if (!waypoints.get(waypoints.size() - 1).isDestination()) {
            return RouteRuleCheckResult.isNotFollowingTheRule("Last waypoint is not a destination place!");
        }

        return RouteRuleCheckResult.isFollowingTheRule();
    };

    RouteRulesPolicy historicalBeforeActiveRule = waypoints -> {
        final int indexOfFirstActiveWaypoint = getIndexOfFirstActiveWaypoint(waypoints);
        final List<Waypoint>[] lists = split(waypoints, indexOfFirstActiveWaypoint);

        if (!lists[0].stream().allMatch(Waypoint::isHistorical)) {
            return RouteRuleCheckResult.isNotFollowingTheRule("Historical waypoints are not before active in list");
        }

        if (!lists[1].stream().allMatch(Waypoint::isActive)) {
            return RouteRuleCheckResult.isNotFollowingTheRule("Active waypoints are not after historical in list");
        }

        return RouteRuleCheckResult.isFollowingTheRule();
    };

    static List<RouteRulesPolicy> allCurrentPolicies() {
        return List.of(onlyOncePolicy, atStartOnlyPolicy, atEndOnlyPolicy, historicalBeforeActiveRule);
    }

    static int getIndexOfFirstActiveWaypoint(final List<Waypoint> waypoints) {
        for (final Waypoint waypoint : waypoints) {
            if (waypoint.isActive()) {
                return waypoints.indexOf(waypoint);
            }
        }

        //TODO: wszystkie historical
        throw new IllegalStateException("Zrob cos");
    }

    static<T> List<T>[] split(final List<T> list, final int index) {
        int[] endpoints = {0, index, list.size()};

        List<List<T>> lists =
            IntStream.rangeClosed(0, 1)
                .mapToObj(i -> list.subList(endpoints[i], endpoints[i + 1]))
                .collect(Collectors.toList());

        return new List[] {lists.get(0), lists.get(1)};
    }

    @Value
    class RouteRuleCheckResult {
        boolean follows;
        String message;

        static RouteRuleCheckResult isFollowingTheRule() {
            return new RouteRuleCheckResult(true, "");
        }

        static RouteRuleCheckResult isNotFollowingTheRule(final @NonNull String reason) {
            return new RouteRuleCheckResult(false, reason);
        }

        boolean isError() {
            return !follows;
        }
    }
}
