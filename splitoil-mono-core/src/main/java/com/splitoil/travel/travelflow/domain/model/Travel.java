package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractEntity;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.travelflow.web.dto.TravelOutputDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

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

    public void addWaypoint(final Waypoint waypoint) {
        route.addWaypoint(waypoint);
    }

    public RouteDto getRoute() {
        return route.toRouteDto();
    }

    public TravelOutputDto toDto() {
        return TravelOutputDto.builder().travelId(getAggregateId()).build();
    }
}
