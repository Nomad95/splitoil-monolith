package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.lobby.web.dto.WaypointDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
class TravelPlan implements JsonEntity, Serializable {

    private final List<Waypoint> route;

    TravelPlan() {
        this.route = new LinkedList<>();
    }

    public boolean canAddWaypoint(final @NonNull Waypoint waypoint) {
        final List<WaypointAddingRulesPolicy> waypointAddingRulesPolicies = WaypointAddingRulesPolicy.allCurrentPolicies();
        for (final WaypointAddingRulesPolicy policy : waypointAddingRulesPolicies) {
            final WaypointAddingRulesPolicy.WaypointAddingCheckResult result = policy.canAddWaypoint(route, waypoint);
            if (result.isError()) {
                log.info("Check can add {} failed. Reason: {}", waypoint.getWaypointType(), result.getMessage());
                return false;
            }
        }

        return true;
    }

    public void addWaypoint(final @NonNull Waypoint waypoint) {
        if (!canAddWaypoint(waypoint)) {
            throw new IllegalArgumentException(String.format("Can't add %s waypoint", waypoint.getWaypointType().name()));
        }

        if (waypoint.isDestination()) {
            route.add(waypoint);
            return;
        }

        if (waypoint.isBeginning()) {
            route.add(0, waypoint);
            return;
        }

        addBeforeDestination(waypoint);
        checkRouteConsistency();
    }

    private void addBeforeDestination(final @NonNull Waypoint waypoint) {
        final Waypoint destination = route.stream().filter(Waypoint::isDestination).findFirst()
            .orElseThrow(() -> new IllegalStateException("No destination waypoint present"));
        final int destinationIndex = route.indexOf(destination);

        route.add(destinationIndex, waypoint);
    }

    private void checkRouteConsistency() {
        final List<RouteRulesPolicy> routeRulesPolicies = RouteRulesPolicy.allCurrentPolicies();
        for (final RouteRulesPolicy routeRule : routeRulesPolicies) {
            final RouteRulesPolicy.RouteRuleCheckResult result = routeRule.followsTheRule(route);
            if (result.isError()) {
                throw new IllegalStateException(String.format("Route is inconsistent! Reason: %s", result.getMessage()));
            }
        }
    }

    RouteDto toDto() {
        final List<WaypointDto> waypoints = route.stream().map(Waypoint::toDto).collect(Collectors.toUnmodifiableList());

        return RouteDto.builder().waypoints(waypoints).build();
    }
}
