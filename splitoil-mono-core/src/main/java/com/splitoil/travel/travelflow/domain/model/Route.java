package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.lobby.web.dto.WaypointDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Route implements JsonEntity, Serializable {

    private final LinkedList<Waypoint> waypoints = new LinkedList<>();

    private TravelId travelId;

    Route(final TravelId travelId) {
        this.travelId = travelId;
    }

    List<Waypoint> waypoints() {
        return Collections.unmodifiableList(waypoints);
    }

    public boolean canAddWaypoint(final @NonNull Waypoint waypoint) {
        final List<WaypointAddingRulesPolicy> waypointAddingRulesPolicies = WaypointAddingRulesPolicy.allCurrentPolicies();
        for (final WaypointAddingRulesPolicy policy : waypointAddingRulesPolicies) {
            final WaypointAddingRulesPolicy.WaypointAddingCheckResult result = policy.canAddWaypoint(waypoints, waypoint);
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
            waypoints.add(waypoint);
            return;
        }

        if (waypoint.isBeginning()) {
            waypoints.add(0, waypoint);
            return;
        }

        addBeforeDestination(waypoint);
        checkRouteConsistency();
    }

    private void addBeforeDestination(final @NonNull Waypoint waypoint) {
        final Waypoint destination = waypoints.stream().filter(Waypoint::isDestination).findFirst()
            .orElseThrow(() -> new IllegalStateException("No destination waypoint present"));
        final int destinationIndex = waypoints.indexOf(destination);

        waypoints.add(destinationIndex, waypoint);
    }

    private void checkRouteConsistency() {
        final List<RouteRulesPolicy> routeRulesPolicies = RouteRulesPolicy.allCurrentPolicies();
        for (final RouteRulesPolicy routeRule : routeRulesPolicies) {
            final RouteRulesPolicy.RouteRuleCheckResult result = routeRule.followsTheRule(waypoints);
            if (result.isError()) {
                throw new IllegalStateException(String.format("Route is inconsistent! Reason: %s", result.getMessage()));
            }
        }
    }

    RouteDto toRouteDto() {
        final List<WaypointDto> waypoints = this.waypoints.stream().map(Waypoint::toDto).collect(Collectors.toUnmodifiableList());

        return RouteDto.builder().waypoints(waypoints).build();
    }

    boolean waypointExists(final @NonNull UUID waypointId) {
        return waypoints.stream().map(Waypoint::getId).anyMatch(id -> id.equals(waypointId));
    }

    void changeLocation(final @NonNull UUID waypointId, final @NonNull GeoPoint newLocation) {
        if (!waypointExists(waypointId)) {
            throw new IllegalArgumentException(String.format("Waypoint %s doesn't exist in travel %s", waypointId, travelId()));
        }

        final Waypoint waypoint = findWaypoint(waypointId);
        if (waypoint.isHistorical()) {
            throw new IllegalArgumentException("Cant change location of historical waypoint " + waypointId);
        }

        changeLocation(waypoint, newLocation);
    }

    private void changeLocation(final @NonNull Waypoint waypoint, final @NonNull GeoPoint newLocation) {
        final int replaceIndex = waypoints.indexOf(waypoint);
        final Waypoint changedWaypoint = waypoint.changeLocation(newLocation);
        waypoints.set(replaceIndex, changedWaypoint);
    }

    private Waypoint findWaypoint(final @NonNull UUID waypointId) {
        return waypoints.stream()
            .filter(waypoint -> waypoint.getId().equals(waypointId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Cant find waypoint: " + waypointId));
    }

    public void moveWaypointAfter(final @NonNull UUID rearrangingWaypointId, final @NonNull UUID rearrangeAfterWaypointId) {
        if (!waypointExists(rearrangingWaypointId)) {
            throw new IllegalArgumentException(
                String.format("Cannot rearrange order of waypoint %s. Rearranging waypoint doesn't exist in travel %s", rearrangingWaypointId, travelId()));
        }

        if (!waypointExists(rearrangeAfterWaypointId)) {
            throw new IllegalArgumentException(
                String.format("Cannot rearrange order of waypoint %s. Rearrange after waypoint of id %s doesn't exist in travel %s",
                    rearrangingWaypointId, rearrangeAfterWaypointId, travelId()));
        }

        if (rearrangingWaypointId.equals(rearrangeAfterWaypointId)) {
            return;
        }

        final Waypoint rearranging = findWaypoint(rearrangingWaypointId);
        final Waypoint arrangeAfter = findWaypoint(rearrangeAfterWaypointId);

        final int rearrangingIndex = waypoints.indexOf(rearranging);
        final int rearrangeAfterIndex = waypoints.indexOf(arrangeAfter);

        if (rearrangeAfterIndex == waypoints.size() - 1) {
            throw new IllegalArgumentException("Can't move waypoint after destination place");
        }

        waypoints.remove(rearranging);
        if (rearrangingIndex < rearrangeAfterIndex) {
            waypoints.add(rearrangeAfterIndex, rearranging);
        } else {
            waypoints.add(rearrangeAfterIndex + 1, rearranging);
        }

        checkRouteConsistency();
    }

    public void deleteWaypoint(final @NonNull UUID waypointToDeleteId) {
        if (!waypointExists(waypointToDeleteId)) {
            throw new IllegalArgumentException(
                String.format("Cannot delete %s - it doesn't exist in travel %s", waypointToDeleteId, travelId()));
        }

        final Waypoint waypointToDelete = findWaypoint(waypointToDeleteId);

        if (waypointToDelete.isHistorical()) {
            throw new IllegalArgumentException(String.format("Can't remove historical waypoint %s", waypointToDeleteId));
        }

        if (waypointToDelete.isBeginning()) {
            throw new IllegalArgumentException(String.format("Can't remove beginning place waypoint %s. This waypoint can only be moved", waypointToDeleteId));
        }

        if (waypointToDelete.isDestination()) {
            throw new IllegalArgumentException(String.format("Can't remove destination waypoint %s. This waypoint can only be moved", waypointToDeleteId));
        }

        waypoints.remove(waypointToDelete);
        checkRouteConsistency();
    }

    private UUID travelId() {
        return travelId.getId();
    }

    public boolean hasBeginningAndEndDefined() {
        return waypoints.stream().anyMatch(Waypoint::isBeginning) && waypoints.stream().anyMatch(Waypoint::isDestination);
    }
}