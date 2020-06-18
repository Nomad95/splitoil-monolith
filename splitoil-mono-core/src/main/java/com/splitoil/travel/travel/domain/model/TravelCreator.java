package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto;
import com.splitoil.travel.travel.web.dto.GeoPointDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

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

    public Waypoint createParticipantBoardingPlace(final double lon, final double lat, final UUID participantId, final UUID boardingCarId) {
        return Waypoint.participantBoardingPlace(GeoPoint.of(lon, lat), new PassengerBoardingPlaceInfo(participantId, boardingCarId));
    }

    public Waypoint createPassengerExitPlace(final double lon, final double lat, final UUID passengerId, final UUID exitingCarId) {
        return Waypoint.passengerExitPlace(GeoPoint.of(lon, lat), new PassengerExitPlaceInfo(passengerId, exitingCarId));
    }

    public Waypoint createRefuelPlace(final double lon, final double lat, final UUID refuelingCarId) {
        return Waypoint.refuelPlace(GeoPoint.of(lon, lat), new CarRefuelPlaceInfo(refuelingCarId));
    }

    public Waypoint createReseatPlace(final double lon, final double lat, final @NonNull UUID participantId,
        final @NonNull UUID carFrom, final @NonNull UUID carTo) {
        return Waypoint.reseatPlace(GeoPoint.of(lon, lat), new PassengerReseatPlaceInfo(participantId, carFrom, carTo));
    }

    public Waypoint createStopPlace(final double lon, final double lat) {
        return Waypoint.stopPlace(GeoPoint.of(lon, lat));
    }

    public GeoPoint createGeoPoint(final GeoPointDto location) {
        return GeoPoint.of(location.getLon(), location.getLat());
    }

    public CarState createInitialCarState(final @NonNull BigDecimal currentFuelLevel, final int odometer) {
        return new CarState(currentFuelLevel, odometer);
    }
}
