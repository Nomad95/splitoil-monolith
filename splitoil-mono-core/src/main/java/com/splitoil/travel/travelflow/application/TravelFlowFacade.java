package com.splitoil.travel.travelflow.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.travelflow.domain.event.*;
import com.splitoil.travel.travelflow.domain.model.Travel;
import com.splitoil.travel.travelflow.domain.model.TravelCreator;
import com.splitoil.travel.travelflow.domain.model.TravelRepository;
import com.splitoil.travel.travelflow.domain.model.Waypoint;
import com.splitoil.travel.travelflow.web.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@ApplicationService
@AllArgsConstructor
public class TravelFlowFacade {

    private final TravelCreator travelCreator;

    private final TravelRepository travelRepository;

    private final EventPublisher eventPublisher;

    public TravelOutputDto createNewTravel(final @NonNull TravelCreationRequested travelCreationRequest) {
        final Travel newTravel = travelCreator.createNewTravel(travelCreationRequest);

        travelRepository.save(newTravel);
        eventPublisher.publish(new TravelCreated(newTravel.getAggregateId(), newTravel.getLobbyId().getId()));

        return newTravel.toDto();
    }

    public void selectTravelBeginning(final @NonNull SelectTravelBeginningCommand selectTravelBeginningCommand) {
        final Travel travel = travelRepository.getByAggregateId(selectTravelBeginningCommand.getTravelId());
        final GeoPointDto waypointLocation = selectTravelBeginningCommand.getLocation();
        final Waypoint beginningPlace = travelCreator.createBeginningPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(beginningPlace);
        eventPublisher.publish(new TravelBeginningPlaceSelected(travel.getAggregateId(), selectTravelBeginningCommand.getLocation()));
    }

    //TODO: query
    public RouteDto getRoute(final @NonNull UUID uuid) {
        final Travel travel = travelRepository.getByAggregateId(uuid);

        return travel.getRoute();
    }

    public void selectTravelDestination(final @NonNull SelectTravelDestinationCommand selectTravelDestinationCommand) {
        final Travel travel = travelRepository.getByAggregateId(selectTravelDestinationCommand.getTravelId());
        final GeoPointDto waypointLocation = selectTravelDestinationCommand.getLocation();
        final Waypoint destinationPlace = travelCreator.createDestinationPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(destinationPlace);
        eventPublisher.publish(new TravelDestinationPlaceSelected(travel.getAggregateId(), selectTravelDestinationCommand.getLocation()));
    }

    public void addReseatPlace(final @NonNull AddReseatPlaceCommand addReseatPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addReseatPlaceCommand.getTravelId());
        final GeoPointDto waypointLocation = addReseatPlaceCommand.getLocation();
        final Waypoint reseatPlace = travelCreator.createReseatPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(reseatPlace);
        eventPublisher.publish(
            new TravelReseatPlaceAdded(
                travel.getAggregateId(),
                addReseatPlaceCommand.getLocation(),
                addReseatPlaceCommand.getCarFrom(),
                addReseatPlaceCommand.getCarTo(),
                addReseatPlaceCommand.getParticipantId()));
    }

    public void addRefuelPlace(final @NonNull AddRefuelPlaceCommand addRefuelPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addRefuelPlaceCommand.getTravelId());
        final GeoPointDto waypointLocation = addRefuelPlaceCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createRefuelPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);//TODO: nie znamy ceny i wartosci przed zatankowaniem xDDD
        eventPublisher.publish(
            new TravelRefuelPlaceAdded(
                travel.getAggregateId(),
                addRefuelPlaceCommand.getLocation(),
                addRefuelPlaceCommand.getCost(),
                addRefuelPlaceCommand.getFuelAmountInLitres()));
    }

    public void addStopPlace(final @NonNull AddStopPlaceCommand addStopPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addStopPlaceCommand.getTravelId());
        final GeoPointDto waypointLocation = addStopPlaceCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createStopPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);
        eventPublisher.publish(
            new TravelStopPlaceAdded(
                travel.getAggregateId(),
                addStopPlaceCommand.getLocation()));
    }

    public void addBoardingPlace(final @NonNull AddParticipantBoardingPlaceCommand addParticipantBoardingPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addParticipantBoardingPlaceCommand.getTravelId());
        final GeoPointDto waypointLocation = addParticipantBoardingPlaceCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createParticipantBoardingPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);
        eventPublisher.publish(
            new TravelParticipantBoardingPlaceAdded(
                travel.getAggregateId(),
                addParticipantBoardingPlaceCommand.getLocation(),
                addParticipantBoardingPlaceCommand.getPassengerId(),
                addParticipantBoardingPlaceCommand.getCarId()));
    }

    public void addExitPlace(final AddParticipantExitPlaceCommand addParticipantExitPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addParticipantExitPlaceCommand.getTravelId());
        final GeoPointDto waypointLocation = addParticipantExitPlaceCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createPassengerExitPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);
        eventPublisher.publish(
            new TravelParticipantExitPlaceAdded(
                travel.getAggregateId(),
                addParticipantExitPlaceCommand.getLocation(),
                addParticipantExitPlaceCommand.getPassengerId(),
                addParticipantExitPlaceCommand.getCarId()));
    }

    public void addCheckpoint(final AddCheckpointCommand addCheckpointCommand) {
        final Travel travel = travelRepository.getByAggregateId(addCheckpointCommand.getTravelId());
        final GeoPointDto waypointLocation = addCheckpointCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createCheckpoint(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);
        eventPublisher.publish(
            new TravelCheckpointAdded(
                travel.getAggregateId(),
                addCheckpointCommand.getLocation()));
    }
}
