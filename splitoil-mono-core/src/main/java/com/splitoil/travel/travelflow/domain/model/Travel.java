package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractEntity;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.travelflow.web.dto.TravelOutputDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Travel extends AbstractEntity {

    Travel(final LobbyId lobbyId, final TravelParticipants travelParticipants, final Route route) {
        this.lobbyId = lobbyId;
        this.travelParticipants = travelParticipants;
        this.route = route;
    }

    @AttributeOverride(name = "id", column = @Column(name = "lobby_id"))
    private LobbyId lobbyId;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.travel.travelflow.domain.model.Route"),
          })
    private Route route;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.travel.travelflow.domain.model.TravelParticipants"),
          })
    private TravelParticipants travelParticipants;

    public void addWaypoint(final @NonNull Waypoint waypoint) {
        route.addWaypoint(waypoint);
    }

    public RouteDto getRoute() {
        return route.toRouteDto();
    }

    public TravelOutputDto toDto() {
        return TravelOutputDto.builder().travelId(getAggregateId()).build();
    }

    public void moveWaypoint(final @NonNull UUID waypointId, final @NonNull GeoPoint newLocation) {
        if (!route.waypointExists(waypointId)) {
            throw new IllegalArgumentException(String.format("Waypoint %s doesn't exist in travel %s", waypointId, getAggregateId()));
        }

        route.changeLocation(waypointId, newLocation);
    }

    public void moveWaypointAfter(final @NonNull UUID rearrangingWaypointId, final @NonNull UUID rearrangeAfterWaypointId) {
        if (!route.waypointExists(rearrangingWaypointId)) {
            throw new IllegalArgumentException(
                String.format("Cannot rearrange order of waypoint %s. Rearranging waypoint doesn't exist in travel %s", rearrangingWaypointId, getAggregateId()));
        }

        if (!route.waypointExists(rearrangeAfterWaypointId)) {
            throw new IllegalArgumentException(
                String.format("Cannot rearrange order of waypoint %s. Rearrange after waypoint of id %s doesn't exist in travel %s",
                    rearrangingWaypointId, rearrangeAfterWaypointId, getAggregateId()));
        }

        route.moveWaypointAfter(rearrangingWaypointId, rearrangeAfterWaypointId);
    }

    public void deleteWaypoint(final @NonNull UUID waypointToDeleteId) {
        if (!route.waypointExists(waypointToDeleteId)) {
            throw new IllegalArgumentException(
                String.format("Cannot delete %s - it doesn't exist in travel %s", waypointToDeleteId, getAggregateId()));
        }

        route.deleteWaypoint(waypointToDeleteId);
    }
}
