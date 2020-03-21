package com.splitoil.travel.lobby.web;

import com.splitoil.car.CarController;
import com.splitoil.shared.dto.Result;
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
        final Link addCarToLobby = linkTo(CarController.class).slash("car").withRel("add-car-to-lobby");
        final Link getLobbyInfo = linkTo(CarController.class).slash(outputDto.getLobbyId().toString()).withRel("get-lobby-info");

        return new EntityModel<>(outputDto)
            .add(addCarToLobby)
            .add(getLobbyInfo)
            .add(self);
    }

    @PostMapping("car")
    public EntityModel<Result> addCarToLobby(@RequestBody @Valid @NonNull final AddCarToTravelCommand addCarToTravelCommand) {
        final Result result = lobbyService.addCarToLobby(addCarToTravelCommand);

        final Link self = linkTo(LobbyController.class).slash("car").withSelfRel();
        final Link changeTopRate = linkTo(LobbyController.class).slash("topRate").withRel("set-travel-top-rate");
        final Link changeCurrency = linkTo(LobbyController.class).slash("currency").withRel("change-default-currency");

        return new EntityModel<>(result)
            .add(self)
            .add(changeCurrency)
            .add(changeTopRate);
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
        final Link lobbyDetails = linkTo(LobbyController.class).withRel("lobby-details");
        final Link changeCurrency = linkTo(LobbyController.class).slash("currency").withRel("change-default-currency");

        return new EntityModel<>(lobby)
            .add(self)
            .add(lobbyDetails)
            .add(changeCurrency);
    }

    @PostMapping("currency")
    public EntityModel<LobbyOutputDto> changeTravelDefaultCurrency(@RequestBody @Valid @NonNull final ChangeTravelDefaultCurrencyCommand changeTravelDefaultCurrencyCommand) {
        final LobbyOutputDto lobby = lobbyService.changeTravelDefaultCurrency(changeTravelDefaultCurrencyCommand);

        final Link self = linkTo(LobbyController.class).slash("currency").withSelfRel();
        final Link lobbyDetails = linkTo(LobbyController.class).withRel("lobby-details");
        final Link changeTopRate = linkTo(LobbyController.class).slash("topRate").withRel("set-travel-top-rate");

        return new EntityModel<>(lobby)
            .add(self)
            .add(lobbyDetails)
            .add(changeTopRate);
    }
}
