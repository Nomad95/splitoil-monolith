package com.splitoil.travel.travel.domain.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.travel.web.dto.waypoint.WaypointAdditionalInfoPayload;

import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PassengerBoardingPlaceInfo.class, name = "passengerBoarding"),
    @JsonSubTypes.Type(value = PassengerExitPlaceInfo.class, name = "passengerExit"),
    @JsonSubTypes.Type(value = PassengerReseatPlaceInfo.class, name = "passengerReseat"),
    @JsonSubTypes.Type(value = CarRefuelPlaceInfo.class, name = "carRefuel"),
    @JsonSubTypes.Type(value = EmptyInfo.class, name = "emptyInfo")
})
abstract class WaypointAdditionalInfo implements Serializable, JsonEntity {

    abstract WaypointAdditionalInfoPayload toDto();
}
