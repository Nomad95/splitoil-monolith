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

    Travel(final LobbyId lobbyId, final TravelParticipants travelParticipants) {
        this.lobbyId = lobbyId;
        this.travelParticipants = travelParticipants;
        this.travelFlow = new TravelFlow();
        this.travelPlan = new TravelPlan();
    }

    @AttributeOverride(name = "id", column = @Column(name = "lobby_id"))
    private LobbyId lobbyId;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.travel.travelplan.domain.model.TravelFlow"),
          })
    private TravelFlow travelFlow;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.travel.travelplan.domain.model.TravelPlan"),
          })
    private TravelPlan travelPlan;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.travel.travelplan.domain.model.TravelParticipants"),
          })
    private TravelParticipants travelParticipants;

    public TravelOutputDto toDto() {
        return TravelOutputDto.builder().travelId(getAggregateId()).build();
    }

    public void addWaypoint(final Waypoint waypoint) {
        travelPlan.addWaypoint(waypoint);
    }

    public RouteDto getRoute() {
        return travelPlan.toDto();
    }
}
