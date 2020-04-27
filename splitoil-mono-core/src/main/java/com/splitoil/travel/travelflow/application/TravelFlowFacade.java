package com.splitoil.travel.travelflow.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.travel.lobby.domain.event.TravelCreationRequested;
import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.travelflow.domain.event.TravelBeginningPlaceSelected;
import com.splitoil.travel.travelflow.domain.event.TravelCreated;
import com.splitoil.travel.travelflow.domain.event.TravelDestinationPlaceSelected;
import com.splitoil.travel.travelflow.domain.model.Travel;
import com.splitoil.travel.travelflow.domain.model.TravelCreator;
import com.splitoil.travel.travelflow.domain.model.TravelRepository;
import com.splitoil.travel.travelflow.domain.model.Waypoint;
import com.splitoil.travel.travelflow.web.dto.GeoPointDto;
import com.splitoil.travel.travelflow.web.dto.SelectTravelBeginningCommand;
import com.splitoil.travel.travelflow.web.dto.SelectTravelDestinationCommand;
import com.splitoil.travel.travelflow.web.dto.TravelOutputDto;
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

    public void selectTravelDestination(final SelectTravelDestinationCommand selectTravelDestinationCommand) {
        final Travel travel = travelRepository.getByAggregateId(selectTravelDestinationCommand.getTravelId());
        final GeoPointDto waypointLocation = selectTravelDestinationCommand.getLocation();
        final Waypoint destinationPlace = travelCreator.createDestinationPlace(waypointLocation.getLon(), waypointLocation.getLat());

        travel.addWaypoint(destinationPlace);
        eventPublisher.publish(new TravelDestinationPlaceSelected(travel.getAggregateId(), selectTravelDestinationCommand.getLocation()));
    }
}
