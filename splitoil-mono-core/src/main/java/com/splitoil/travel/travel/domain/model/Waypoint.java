package com.splitoil.travel.travel.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.travel.web.dto.GeoPointDto;
import com.splitoil.travel.travel.web.dto.WaypointDto;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Waypoint implements JsonEntity, Serializable {

    private UUID id = UUID.randomUUID();

    @NonNull
    private GeoPoint location;

    @NonNull
    private WaypointType waypointType;

    private boolean historical = false;

    @NonNull
    private WaypointAdditionalInfo additionalInfo;

    Waypoint(final GeoPoint location, final WaypointType waypointType, final boolean historical) {
        this.location = location;
        this.waypointType = waypointType;
        this.historical = historical;
        this.additionalInfo = new EmptyInfo();
    }

    Waypoint(final UUID id, final GeoPoint location, final WaypointType waypointType, final boolean historical) {
        this.id = id;
        this.location = location;
        this.waypointType = waypointType;
        this.historical = historical;
        this.additionalInfo = new EmptyInfo();
    }

    Waypoint(final GeoPoint location, final WaypointType waypointType, final boolean historical, final WaypointAdditionalInfo waypointAdditionalInfo) {
        this.location = location;
        this.waypointType = waypointType;
        this.historical = historical;
        this.additionalInfo = waypointAdditionalInfo;
    }

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

    static Waypoint reseatPlace(final @NonNull GeoPoint geoPoint, final WaypointAdditionalInfo additionalInfo) {
        return new Waypoint(geoPoint, WaypointType.RESEAT_PLACE, false, additionalInfo);
    }

    static Waypoint refuelPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.REFUEL_PLACE, false);
    }

    static Waypoint refuelPlace(final @NonNull GeoPoint geoPoint, final WaypointAdditionalInfo additionalInfo) {
        return new Waypoint(geoPoint, WaypointType.REFUEL_PLACE, false, additionalInfo);
    }

    static Waypoint stopPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.STOP_PLACE, false);
    }

    static Waypoint participantBoardingPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.PARTICIPANT_BOARDING_PLACE, false);
    }

    static Waypoint participantBoardingPlace(final @NonNull GeoPoint geoPoint, final WaypointAdditionalInfo additionalInfo) {
        return new Waypoint(geoPoint, WaypointType.PARTICIPANT_BOARDING_PLACE, false, additionalInfo);
    }

    static Waypoint passengerExitPlace(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.PARTICIPANT_EXIT_PLACE, false);
    }

    static Waypoint passengerExitPlace(final @NonNull GeoPoint geoPoint, final WaypointAdditionalInfo additionalInfo) {
        return new Waypoint(geoPoint, WaypointType.PARTICIPANT_EXIT_PLACE, false, additionalInfo);
    }

    static Waypoint checkpoint(final @NonNull GeoPoint geoPoint) {
        return new Waypoint(geoPoint, WaypointType.CHECKPOINT, false);
    }

    public Waypoint changeLocation(final @NonNull GeoPoint newLocation) {
        return new Waypoint(id, newLocation, waypointType, historical);
    }

    enum WaypointType {
        BEGINNING_PLACE, DESTINATION_PLACE, RESEAT_PLACE, REFUEL_PLACE, STOP_PLACE, PARTICIPANT_BOARDING_PLACE, PARTICIPANT_EXIT_PLACE, CHECKPOINT
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
            .id(id)
            .historical(historical)
            .location(GeoPointDto.of(location.getLon(), location.getLat()))
            .waypointType(waypointType.name())
            .additionalInfo(additionalInfo.toDto())
            .build();
    }
}
