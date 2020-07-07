package com.splitoil.travel.flowcontrol.domain.model;

import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractEntity;
import com.splitoil.travel.flowcontrol.web.dto.FlowControlOutputDto;
import com.splitoil.travel.flowcontrol.web.dto.GpsPointDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.splitoil.travel.flowcontrol.domain.model.Result.REDUNDANT;
import static com.splitoil.travel.flowcontrol.domain.model.Result.SUCCESS;
import static java.util.stream.Collectors.toUnmodifiableList;

@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlowControl extends AbstractEntity {

    @NonNull
    @Getter
    private UUID carId;

    @NonNull
    private UUID travelId;

    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @Parameter(name = JsonUserType.LIST, value = "java.util.List"),
              @Parameter(name = JsonUserType.ELEM_TYPE, value = "com.splitoil.travel.flowcontrol.domain.model.GpsPoint")
          })
    private List<GpsPoint> gpsPoints;

    FlowControl(final @NonNull UUID carId, final @NonNull UUID travelId) {
        this.carId = carId;
        this.travelId = travelId;
        this.gpsPoints = new ArrayList<>();
    }

    public FlowControlOutputDto toDto() {
        return FlowControlOutputDto.builder()
            .id(getAggregateId())
            .carId(carId)
            .travelId(travelId)
            .build();
    }

    public List<GpsPointDto> getGpsPoints() {
        return gpsPoints.stream()
            .map(GpsPoint::toDto)
            .collect(toUnmodifiableList());
    }

    public Result addGpsPoint(final GpsPoint gpsPoint) {
        if (Objects.nonNull(gpsPoint.getAssociatedWaypoint())) {
            final boolean waypointReceived = gpsPoints.stream()
                .filter(point -> Objects.nonNull(point.getAssociatedWaypoint()))
                .anyMatch(point -> Objects.equals(point.getAssociatedWaypoint().getWaypointUuid(), gpsPoint.getAssociatedWaypoint().getWaypointUuid()));
            if (waypointReceived) {
                log.warn("Flow control {} has already acknowledged waypoint {}", getAggregateId(), gpsPoint.getAssociatedWaypoint().getWaypointUuid());
                return REDUNDANT;
            }
        }
        gpsPoints.add(gpsPoint);
        return SUCCESS;
    }
}
