package com.splitoil.travel.lobby.web;

import com.splitoil.car.CarController;
import com.splitoil.travel.lobby.application.LobbyService;
import com.splitoil.travel.lobby.web.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/lobby")
public class LobbyController {

    private LobbyService lobbyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<LobbyOutputDto> createNewLobby(@RequestBody @Valid @NonNull final CreateLobbyCommand createLobbyCommand) {
        final LobbyOutputDto outputDto = lobbyService.createLobby(createLobbyCommand);

        final Link self = linkTo(LobbyController.class).withSelfRel();

        return new EntityModel<>(outputDto)
            .add(addCarToLobbyLink())
            .add(lobbyDetailsLink(outputDto.getLobbyId()))
            .add(self);
    }

    @PostMapping("car")
    public EntityModel<LobbyOutputDto> addCarToLobby(@RequestBody @Valid @NonNull final AddCarToTravelCommand addCarToTravelCommand) {
        final LobbyOutputDto outputDto = lobbyService.addCarToLobby(addCarToTravelCommand);

        final Link self = linkTo(LobbyController.class).slash("car").withSelfRel();

        return new EntityModel<>(outputDto)
            .add(self)
            .add(setLobbyCurrencyLink())
            .add(addCarToLobbyLink())
            .add(addTemporalPassengerLink())
            .add(addPassengerLink())
            .add(changeLobbyTopRateLink());
    }

    @GetMapping("{lobbyId}")
    public EntityModel<LobbyOutputDto> getLobbyDetails(@NonNull @PathVariable("lobbyId") final UUID lobbyId) {
        final LobbyOutputDto lobby = lobbyService.getLobby(lobbyId);

        final Link self = linkTo(LobbyController.class).slash(lobbyId).withSelfRel();

        return new EntityModel<>(lobby)
            .add(self);
    }

    @PostMapping("topRate")
    public EntityModel<LobbyOutputDto> setTravelTopRatePer1km(@RequestBody @Valid @NonNull final SetTravelTopRatePer1kmCommand setTravelTopRatePer1kmCommand) {
        final LobbyOutputDto lobby = lobbyService.setTravelTopRatePer1km(setTravelTopRatePer1kmCommand);

        final Link self = linkTo(LobbyController.class).slash("topRate").withSelfRel();

        return new EntityModel<>(lobby)
            .add(self)
            .add(lobbyDetailsLink(lobby.getLobbyId()))
            .add(setLobbyCurrencyLink())
            .add(addCarToLobbyLink())
            .add(addTemporalPassengerLink())
            .add(assignToCarLink())
            .add(addPassengerLink());
    }

    @PostMapping("currency")
    public EntityModel<LobbyOutputDto> changeTravelDefaultCurrency(
        @RequestBody @Valid @NonNull final ChangeTravelDefaultCurrencyCommand changeTravelDefaultCurrencyCommand) {
        final LobbyOutputDto lobby = lobbyService.changeTravelDefaultCurrency(changeTravelDefaultCurrencyCommand);

        final Link self = linkTo(LobbyController.class).slash("currency").withSelfRel();

        return new EntityModel<>(lobby)
            .add(self)
            .add(lobbyDetailsLink(lobby.getLobbyId()))
            .add(changeLobbyTopRateLink())
            .add(addTemporalPassengerLink())
            .add(assignToCarLink())
            .add(addPassengerLink());
    }

    @PostMapping("participant/passenger")
    public EntityModel<LobbyOutputDto> addPassengerToLobby(@RequestBody @Valid @NonNull final AddPassengerToLobbyCommand addPassengerToLobbyCommand) {
        final LobbyOutputDto lobby = lobbyService.addPassenger(addPassengerToLobbyCommand);

        final Link self = linkTo(LobbyController.class).slash("participant/passenger").withSelfRel();

        return new EntityModel<>(lobby)
            .add(self)
            .add(addCarToLobbyLink())
            .add(setLobbyCurrencyLink())
            .add(addTemporalPassengerLink())
            .add(assignToCarLink())
            .add(lobbyDetailsLink(lobby.getLobbyId()))
            .add(changeLobbyTopRateLink());
    }

    @PostMapping("participant/temporalpassenger")
    public EntityModel<LobbyOutputDto> addExternalPassengerToLobby(
        @RequestBody @Valid @NonNull final AddTemporalPassengerToLobbyCommand addTemporalPassengerToLobbyCommand) {
        final LobbyOutputDto lobby = lobbyService.addTemporalPassenger(addTemporalPassengerToLobbyCommand);

        final Link self = linkTo(LobbyController.class).slash("participant/temporalpassenger").withSelfRel();

        return new EntityModel<>(lobby)
            .add(self)
            .add(lobbyDetailsLink(lobby.getLobbyId()))
            .add(addCarToLobbyLink())
            .add(setLobbyCurrencyLink())
            .add(addPassengerLink())
            .add(assignToCarLink())
            .add(changeLobbyTopRateLink());
    }

    @PostMapping("participant/assignToCar")
    public EntityModel<LobbyOutputDto> assignToCar(@RequestBody @Valid @NonNull final AssignToCarCommand assignToCarCommand) {
        final LobbyOutputDto lobby = lobbyService.assignToCar(assignToCarCommand);

        final Link self = linkTo(LobbyController.class).slash("participant/assignToCar").withSelfRel();

        return new EntityModel<>(lobby)
            .add(self)
            .add(lobbyDetailsLink(lobby.getLobbyId()))
            .add(addCarToLobbyLink())
            .add(setLobbyCurrencyLink())
            .add(addPassengerLink())
            .add(addTemporalPassengerLink())
            .add(changeLobbyTopRateLink());
    }

    private Link addTemporalPassengerLink() {
        return linkTo(LobbyController.class).slash("participant/passenger").withRel("add-temporal-passenger");
    }

    private Link assignToCarLink() {
        return linkTo(LobbyController.class).slash("participant/assignToCar").withRel("assign-to-car");
    }

    private Link changeLobbyTopRateLink() {
        return linkTo(LobbyController.class).slash("topRate").withRel("set-travel-top-rate");
    }

    private Link addCarToLobbyLink() {
        return linkTo(CarController.class).slash("car").withRel("add-car-to-lobby");
    }

    private Link setLobbyCurrencyLink() {
        return linkTo(LobbyController.class).slash("currency").withRel("change-default-currency");
    }

    private Link addPassengerLink() {
        return linkTo(LobbyController.class).slash("participant/passenger").withRel("add-passenger");
    }

    private Link lobbyDetailsLink(final UUID lobbyId) {
        return linkTo(CarController.class).slash(lobbyId.toString()).withRel("get-lobby-info");
    }
}
