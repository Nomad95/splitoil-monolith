package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.lobby.web.dto.WaypointDto;
import com.splitoil.travel.travelflow.web.dto.GeoPointDto;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Waypoint implements JsonEntity, Serializable {

    private GeoPoint location;

    private WaypointType waypointType;

    private boolean historical = false;

    boolean is(final @NonNull WaypointType waypointType) {
        return waypointType == this.waypointType;
    }

    static Waypoint beginningPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.BEGINNING_PLACE, false);
    }

    static Waypoint destinationPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.DESTINATION_PLACE, false);
    }

    static Waypoint reseatPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.RESEAT_PLACE, false);
    }

    static Waypoint refuelPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.REFUEL_PLACE, false);
    }

    static Waypoint stopPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.STOP_PLACE, false);
    }

    static Waypoint passengerBoardingPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.PASSENGER_BOARDING_PLACE, false);
    }

    static Waypoint passengerExitPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.PASSENGER_EXIT_PLACE, false);
    }

    static Waypoint checkpoint(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.CHECKPOINT, false);
    }

    enum WaypointType {
        BEGINNING_PLACE, DESTINATION_PLACE, RESEAT_PLACE, REFUEL_PLACE, STOP_PLACE, PASSENGER_BOARDING_PLACE, PASSENGER_EXIT_PLACE, CHECKPOINT
    }

    boolean isBeginning() {
        return WaypointType.BEGINNING_PLACE == this.waypointType;
    }

    boolean isDestination() {
        return WaypointType.DESTINATION_PLACE == this.waypointType;
    }

    boolean isActive() {
        return !historical;
    }

    WaypointDto toDto() {
        return WaypointDto.builder()
            .historical(historical)
            .location(GeoPointDto.of(location.getLon(), location.getLat()))
            .waypointType(waypointType.name())
            .build();
    }
}
