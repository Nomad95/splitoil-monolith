package com.splitoil.travel.travelflow.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.lobby.application.LobbyQuery;
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.travelflow.domain.event.*;
import com.splitoil.travel.travelflow.domain.model.*;
import com.splitoil.travel.travelflow.web.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@ApplicationService
@AllArgsConstructor
public class TravelFlowFacade {

    private final TravelCreator travelCreator;

    private final TravelService travelService;

    private final TravelRepository travelRepository;

    private final EventPublisher eventPublisher;

    private final LobbyQuery lobbyQuery;

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

        final List<UUID> affectedCarIds = List.of(addReseatPlaceCommand.getCarFrom(), addReseatPlaceCommand.getCarFrom());
        final UUID lobbyId = travel.getLobbyId().getId();
        final UUID reseatedParticipantId = addReseatPlaceCommand.getParticipantId();

        final boolean carsExist = lobbyQuery.carsExistInLobby(affectedCarIds, lobbyId);
        final boolean passengerExist = lobbyQuery.participantExistsInLobby(reseatedParticipantId, lobbyId);

        if (!carsExist) {
            throw new IllegalArgumentException("Can't execute the command. Some car are not present in the lobby");
        }

        if (!passengerExist) {
            throw new IllegalArgumentException("Can't execute the command. Passenger is not present in the lobby");
        }

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

        final UUID refuelingCarId = addRefuelPlaceCommand.getCarBeingRefueld();
        final UUID lobbyId = travel.getLobbyId().getId();

        final boolean carExists = lobbyQuery.carExistInLobby(refuelingCarId, lobbyId);

        if (!carExists) {
            throw new IllegalArgumentException("Can't execute the command. Car is not present in the lobby");
        }

        final GeoPointDto waypointLocation = addRefuelPlaceCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createRefuelPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);
        eventPublisher.publish(
            new TravelRefuelPlaceAdded(
                travel.getAggregateId(),
                addRefuelPlaceCommand.getLocation(),
                addRefuelPlaceCommand.getCarBeingRefueld()));
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


    //TODO: no dobra trzeba pomyśleć co będzie jesli to rozdzielimy na kilka mikroserwisów
    public void addBoardingPlace(final @NonNull AddParticipantBoardingPlaceCommand addParticipantBoardingPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addParticipantBoardingPlaceCommand.getTravelId());

        final UUID participantId = addParticipantBoardingPlaceCommand.getPassengerId();
        final UUID boardingCarId = addParticipantBoardingPlaceCommand.getCarId();
        final UUID lobbyId = travel.getLobbyId().getId();

        final boolean participantExist = lobbyQuery.participantExistsInLobby(participantId, lobbyId);
        final boolean carExistInLobby = lobbyQuery.carExistInLobby(boardingCarId, lobbyId);

        if (!participantExist) {
            throw new IllegalArgumentException("Can't execute the command. Participant is not present in the lobby");
        }

        if (!carExistInLobby) {
            throw new IllegalArgumentException("Can't execute the command. Car is not present in the lobby");
        }

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

    public void addExitPlace(final @NonNull AddParticipantExitPlaceCommand addParticipantExitPlaceCommand) {
        final Travel travel = travelRepository.getByAggregateId(addParticipantExitPlaceCommand.getTravelId());

        final UUID participantId = addParticipantExitPlaceCommand.getPassengerId();
        final UUID boardingCarId = addParticipantExitPlaceCommand.getCarId();
        final UUID lobbyId = travel.getLobbyId().getId();

        final boolean participantExist = lobbyQuery.participantExistsInLobby(participantId, lobbyId);
        final boolean carExistInLobby = lobbyQuery.carExistInLobby(boardingCarId, lobbyId);

        if (!participantExist) {
            throw new IllegalArgumentException("Can't execute the command. Participant is not present in the lobby");
        }

        if (!carExistInLobby) {
            throw new IllegalArgumentException("Can't execute the command. Car is not present in the lobby");
        }

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

    public void addCheckpoint(final @NonNull AddCheckpointCommand addCheckpointCommand) {
        final Travel travel = travelRepository.getByAggregateId(addCheckpointCommand.getTravelId());
        final GeoPointDto waypointLocation = addCheckpointCommand.getLocation();
        final Waypoint refuelPlace = travelCreator.createCheckpoint(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(refuelPlace);
        eventPublisher.publish(
            new TravelCheckpointAdded(
                travel.getAggregateId(),
                addCheckpointCommand.getLocation()));
    }

    public void moveWaypoint(final @NonNull MoveWaypointCommand moveWaypointCommand) {
        final Travel travel = travelRepository.getByAggregateId(moveWaypointCommand.getTravelId());
        final GeoPoint newLocation = travelCreator.createGeoPoint(moveWaypointCommand.getLocation());
        final UUID waypointId = moveWaypointCommand.getWaypointId();

        travel.moveWaypoint(waypointId, newLocation);
        eventPublisher.publish(new WaypointLocationMoved(
            travel.getAggregateId(),
            moveWaypointCommand.getWaypointId(),
            moveWaypointCommand.getLocation()
        ));
    }

    public void changeWaypointOrder(final @NonNull ChangeOrderWaypointCommand changeOrderWaypointCommand) {
        final Travel travel = travelRepository.getByAggregateId(changeOrderWaypointCommand.getTravelId());
        final UUID rearrangingWaypointId = changeOrderWaypointCommand.getRearrangingWaypoint();
        final UUID rearrangeAfterWaypointId = changeOrderWaypointCommand.getRearrangeAfterWaypoint();

        travel.moveWaypointAfter(rearrangingWaypointId, rearrangeAfterWaypointId);
        eventPublisher.publish(new WaypointOrderChanged(
            travel.getAggregateId(),
            rearrangingWaypointId,
            rearrangeAfterWaypointId
        ));
    }

    public void deleteWaypoint(final @NonNull DeleteWaypointCommand deleteWaypointCommand) {
        final Travel travel = travelRepository.getByAggregateId(deleteWaypointCommand.getTravelId());
        final UUID waypointToDeleteId = deleteWaypointCommand.getWaypointId();

        travel.deleteWaypoint(waypointToDeleteId);
        eventPublisher.publish(new WaypointDeleted(
            travel.getAggregateId(),
            waypointToDeleteId
        ));
    }

    public void confirmPlan(final @NonNull ConfirmTravelPlanCommand confirmTravelPlanCommand) {
        final Travel travel = travelRepository.getByAggregateId(confirmTravelPlanCommand.getTravelId());

        travel.confirmTravelPlan();
        eventPublisher.publish(new TravelPlanConfirmed(
            travel.getAggregateId()
        ));
    }

    public TravelOutputDto getTravel(final @NonNull UUID travelId) {
        return travelRepository.getByAggregateId(travelId).toDto();
    }

    public void setCarInitialState(final @NonNull SetCarInitialStateCommand setCarInitialStateCommand) {
        final Travel travel = travelRepository.getByAggregateId(setCarInitialStateCommand.getTravelId());

        final UUID lobbyId = travel.getLobbyId().getId();
        final UUID carId = setCarInitialStateCommand.getCarId();

        final boolean carExistInLobby = lobbyQuery.carExistInLobby(carId, lobbyId);

        if (!carExistInLobby) {
            throw new IllegalArgumentException("Can't execute the command. Car is not present in the lobby");
        }

        final CarState initialCarState = travelCreator.createInitialCarState(
            setCarInitialStateCommand.getCurrentFuelLevel(),
            setCarInitialStateCommand.getOdometer());
        travel.setCarsInitialState(carId, initialCarState);
        eventPublisher.publish(new CarInitialStateSet(
            travel.getAggregateId(),
            carId,
            setCarInitialStateCommand.getCurrentFuelLevel(),
            setCarInitialStateCommand.getOdometer()
        ));
    }

    public void startTravel(final @NonNull StartTravelCommand startTravelCommand) {
        final Travel travel = travelRepository.getByAggregateId(startTravelCommand.getTravelId());

        if (!travel.isInConfirmation()) {
            throw new IllegalStateException("Can't start travel when all travel is not in confirmation state");
        }

        final List<UUID> carsIds = lobbyQuery.getLobbyCarsIds(travel.getLobbyId().getId());

        if (!travelService.hasAllCarsStateSet(travel, carsIds)) {
            throw new IllegalStateException("Can't start travel when all cars doesn't have their state set");
        }

        travel.startTravel();
        eventPublisher.publish(new TravelStarted(
            travel.getAggregateId()
        ));
    }
}
