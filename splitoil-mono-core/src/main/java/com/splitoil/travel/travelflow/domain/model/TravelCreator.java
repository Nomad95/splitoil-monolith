package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantForTravelPlanDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;

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

    public Waypoint createPassengerBoardingPlace(final double lon, final double lat) {
        return Waypoint.passengerBoardingPlace(GeoPoint.of(lon, lat));
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
}
