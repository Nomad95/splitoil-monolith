package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto;
import com.splitoil.travel.travel.web.dto.GeoPointDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@AllArgsConstructor
public class TravelCreator {

    public Travel createNewTravel(final @NonNull TravelCreationRequested travelCreationRequest) {
        final LobbyId lobbyId = LobbyId.of(travelCreationRequest.getAggregateId());

        final TravelParticipants.TravelParticipantsBuilder participantBuilder = TravelParticipants.builder();

        for (final LobbyParticipantForTravelPlanDto lobbyParticipant : travelCreationRequest.getForTravelCreationLobbyDto().getParticipants()) {
            participantBuilder.participant(new TravelParticipant(lobbyParticipant.getUserId(), lobbyParticipant.getAssignedCar()));
        }

        return new Travel(lobbyId, participantBuilder.build());
    }

    public Waypoint createBeginningPlace(final double lon, final double lat) {
        return Waypoint.beginningPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createDestinationPlace(final double lon, final double lat) {
        return Waypoint.destinationPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createCheckpoint(final double lon, final double lat) {
        return Waypoint.checkpoint(GeoPoint.of(lon, lat));
    }

    public Waypoint createParticipantBoardingPlace(final double lon, final double lat) {
        return Waypoint.participantBoardingPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createPassengerExitPlace(final double lon, final double lat) {
        return Waypoint.passengerExitPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createRefuelPlace(final double lon, final double lat) {
        return Waypoint.refuelPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createReseatPlace(final double lon, final double lat) {
        return Waypoint.reseatPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createStopPlace(final double lon, final double lat) {
        return Waypoint.stopPlace(GeoPoint.of(lon, lat));
    }

    public Waypoint createWaypointByType(final GeoPointDto location, final String waypointType) {
        final Waypoint.WaypointType type = Waypoint.WaypointType.valueOf(waypointType);
        switch (type) {
            case CHECKPOINT:
                return createCheckpoint(location.getLon(), location.getLat());
            case RESEAT_PLACE:
                return createReseatPlace(location.getLon(), location.getLat());
            case BEGINNING_PLACE:
                return createBeginningPlace(location.getLon(), location.getLat());
            case DESTINATION_PLACE:
                return createDestinationPlace(location.getLon(), location.getLat());
            case PARTICIPANT_BOARDING_PLACE:
                return createParticipantBoardingPlace(location.getLon(), location.getLat());
            case STOP_PLACE:
                return createStopPlace(location.getLon(), location.getLat());
            case REFUEL_PLACE:
                return createRefuelPlace(location.getLon(), location.getLat());
            case PARTICIPANT_EXIT_PLACE:
                return createPassengerExitPlace(location.getLon(), location.getLat());
        }

        throw new IllegalArgumentException("No waypoint for type: " + waypointType);
    }

    public GeoPoint createGeoPoint(final GeoPointDto location) {
        return GeoPoint.of(location.getLon(), location.getLat());
    }

    public CarState createInitialCarState(final @NonNull BigDecimal currentFuelLevel, final int odometer) {
        return new CarState(currentFuelLevel, odometer);
    }
}
