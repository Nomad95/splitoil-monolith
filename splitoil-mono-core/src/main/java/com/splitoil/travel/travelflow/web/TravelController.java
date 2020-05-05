package com.splitoil.travel.travelflow.web;

import com.splitoil.travel.lobby.web.dto.RouteDto;
import com.splitoil.travel.travelflow.application.TravelFlowFacade;
import com.splitoil.travel.travelflow.web.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/travel")
public class TravelController {

    private final TravelFlowFacade travelFlowFacade;

    @PostMapping("route/beginning")
    public EntityModel<RouteDto> selectTravelBeginning(@RequestBody @Valid @NonNull final SelectTravelBeginningCommand selectTravelBeginningCommand) {
        travelFlowFacade.selectTravelBeginning(selectTravelBeginningCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(selectTravelBeginningCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("beginning").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(selectTravelBeginningCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/destination")
    public EntityModel<RouteDto> selectTravelDestination(@RequestBody @Valid @NonNull final SelectTravelDestinationCommand selectTravelDestinationCommand) {
        travelFlowFacade.selectTravelDestination(selectTravelDestinationCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(selectTravelDestinationCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("destination").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(selectTravelDestinationCommand.getTravelId()))
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/reseat")
    public EntityModel<RouteDto> selectParticipantChangePlace(@RequestBody @Valid @NonNull final AddReseatPlaceCommand addReseatPlaceCommand) {
        travelFlowFacade.addReseatPlace(addReseatPlaceCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(addReseatPlaceCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("reseat").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(addReseatPlaceCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/refuel")
    public EntityModel<RouteDto> selectParticipantRefuelPlace(@RequestBody @Valid @NonNull final AddRefuelPlaceCommand addRefuelPlaceCommand) {
        travelFlowFacade.addRefuelPlace(addRefuelPlaceCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(addRefuelPlaceCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("refuel").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(addRefuelPlaceCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/stop")
    public EntityModel<RouteDto> selectStopPlace(@RequestBody @Valid @NonNull final AddStopPlaceCommand addStopPlaceCommand) {
        travelFlowFacade.addStopPlace(addStopPlaceCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(addStopPlaceCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("stop").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(addStopPlaceCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addChangeWaypointOrderLink())
            .add(addMoveWaypointLocationLink())
            .add(self);
    }

    @PostMapping("route/boarding")
    public EntityModel<RouteDto> selectParticipantBoardingPlace(@RequestBody @Valid @NonNull final AddParticipantBoardingPlaceCommand addParticipantBoardingPlaceCommand) {
        travelFlowFacade.addBoardingPlace(addParticipantBoardingPlaceCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(addParticipantBoardingPlaceCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("boarding").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(addParticipantBoardingPlaceCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addChangeWaypointOrderLink())
            .add(addMoveWaypointLocationLink())
            .add(self);
    }

    @PostMapping("route/exit")
    public EntityModel<RouteDto> selectParticipantExitPlace(@RequestBody @Valid @NonNull final AddParticipantExitPlaceCommand addParticipantExitPlaceCommand) {
        travelFlowFacade.addExitPlace(addParticipantExitPlaceCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(addParticipantExitPlaceCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("exit").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(addParticipantExitPlaceCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/checkpoint")
    public EntityModel<RouteDto> selectCheckpointPlace(@RequestBody @Valid @NonNull final AddCheckpointCommand addCheckpointCommand) {
        travelFlowFacade.addCheckpoint(addCheckpointCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(addCheckpointCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("checkpoint").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(addCheckpointCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/changelocation")
    public EntityModel<RouteDto> changeWaypointLocation(@RequestBody @Valid @NonNull final MoveWaypointCommand moveWaypointCommand) {
        travelFlowFacade.moveWaypoint(moveWaypointCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(moveWaypointCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("changelocation").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(moveWaypointCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addChangeWaypointOrderLink())
            .add(self);
    }

    @PostMapping("route/changeorder")
    public EntityModel<RouteDto> changeWaypointLocation(@RequestBody @Valid @NonNull final ChangeOrderWaypointCommand changeOrderWaypointCommand) {
        travelFlowFacade.changeWaypointOrder(changeOrderWaypointCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(changeOrderWaypointCommand.getTravelId());

        final Link self = linkTo(TravelController.class).slash("route").slash("changeorder").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(getRouteLink(changeOrderWaypointCommand.getTravelId()))
            .add(addTravelDestinationLink())
            .add(addTravelBeginningLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(self);
    }

    @GetMapping("route/{travelId}")
    public EntityModel<RouteDto> getRoute(@PathVariable("travelId") final UUID travelId) {
        final RouteDto route = travelFlowFacade.getRoute(travelId);

        final Link self = linkTo(TravelController.class).slash("route").slash(travelId).withSelfRel();

        return new EntityModel<>(route)
            .add(addTravelBeginningLink())
            .add(addTravelDestinationLink())
            .add(addTravelReseatLink())
            .add(addRefuelPlaceLink())
            .add(addStopPlaceLink())
            .add(addBoardingPlaceLink())
            .add(addExitPlaceLink())
            .add(addCheckpointPlaceLink())
            .add(addMoveWaypointLocationLink())
            .add(self);
    }

    private Link addTravelBeginningLink() {
        return linkTo(TravelController.class).slash("route").slash("beginning").withRel("add-travel-beginning");
    }

    private Link addChangeWaypointOrderLink() {
        return linkTo(TravelController.class).slash("route").slash("beginning").withRel("change-waypoint-order");
    }

    private Link addTravelDestinationLink() {
        return linkTo(TravelController.class).slash("route").slash("destination").withRel("add-travel-destination");
    }

    private Link addTravelReseatLink() {
        return linkTo(TravelController.class).slash("route").slash("reseat").withRel("add-reseat-place");
    }

    private Link addRefuelPlaceLink() {
        return linkTo(TravelController.class).slash("route").slash("refuel").withRel("add-refuel-place");
    }

    private Link addStopPlaceLink() {
        return linkTo(TravelController.class).slash("route").slash("stop").withRel("add-stop-place");
    }

    private Link addBoardingPlaceLink() {
        return linkTo(TravelController.class).slash("route").slash("stop").withRel("add-boarding-place");
    }

    private Link addExitPlaceLink() {
        return linkTo(TravelController.class).slash("route").slash("exit").withRel("add-exit-place");
    }

    private Link addCheckpointPlaceLink() {
        return linkTo(TravelController.class).slash("route").slash("checkpoint").withRel("add-checkpoint-place");
    }

    private Link addMoveWaypointLocationLink() {
        return linkTo(TravelController.class).slash("route").slash("changelocation").withRel("change-waypoint-location");
    }

    private Link getRouteLink(final UUID travelId) {
        return linkTo(TravelController.class).slash("route").slash(travelId).withRel("get-travel-route");
    }

}
