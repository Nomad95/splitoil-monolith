package com.splitoil.gasstation;

import com.splitoil.gasstation.domain.GasStationsFacade;
import com.splitoil.gasstation.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/gas-station")
class GasStationController {

    private GasStationsFacade gasStationsFacade;

    @PostMapping(value = "/observe")
    public GasStationIdDto addGasStationToObservable(@RequestBody @NonNull @Valid final AddToObservableDto command) {
        final GasStationIdDto gasStationIdDto = gasStationsFacade.addToObservables(command);

        final Link getObservedLink = linkTo(methodOn(GasStationController.class).getObservedGasStations(command.getDriver().getDriverId())).withRel("observed");
        gasStationIdDto.add(getObservedLink);

        return gasStationIdDto;
    }

    @GetMapping(value = "/observe/driver/{driverId}")
    public CollectionModel<EntityModel<GasStationIdDto>> getObservedGasStations(final @NonNull @PathVariable Long driverId) {
        final List<GasStationIdDto> observedGasStations = gasStationsFacade.getObservedGasStations(driverId);

        return CollectionModel.wrap(observedGasStations);
    }

    @PostMapping(value = "/rate")
    public EntityModel<BigDecimal> rateGasStation(@RequestBody @NonNull @Valid final AddRatingDto command) {
        final BigDecimal newRating = gasStationsFacade.rateGasStation(command);

        final Link getRating = linkTo(GasStationController.class).slash("rating").withRel("rating");
        final Link self = linkTo(GasStationController.class).slash("rate").withSelfRel();

        return new EntityModel<>(newRating).add(getRating).add(self);
    }

    @PostMapping(value = "/rating")
    public EntityModel<BigDecimal> getGasStationRating(final @RequestBody GasStationIdDto gasStationIdDto) {
        final BigDecimal rating = gasStationsFacade.getRating(gasStationIdDto);

        final Link self = linkTo(GasStationController.class).slash("rating").withSelfRel();

        return new EntityModel<>(rating).add(self);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/gas-price")
    public EntityModel<UUID> addPetrolPriceToGasStation(final @RequestBody @NonNull AddPetrolPriceDto addPetrolPriceDto) {
        final UUID commandUuid = gasStationsFacade.addPetrolPrice(addPetrolPriceDto);

        final Link accept = linkTo(GasStationController.class).slash("gas-price").slash("accept").withRel("accept");
        final Link self = linkTo(GasStationController.class).slash("gas-price").withSelfRel();

        return new EntityModel<>(commandUuid).add(self).add(accept);
    }

    @PostMapping(value = "/gas-price/accept")
    public void acceptGasStationPrice(final @RequestBody AcceptPetrolPriceDto addPetrolPriceDto) {
        gasStationsFacade.acceptPetrolPrice(addPetrolPriceDto);
    }

    @PostMapping(value = "/gas-price/current")
    public EntityModel<BigDecimal> getPetrolPriceFromGasStation(final @RequestBody GetPetrolPriceDto getPetrolPriceDto) {
        final BigDecimal currentPetrolPrice = gasStationsFacade.getCurrentPetrolPrice(getPetrolPriceDto);

        final Link self = linkTo(GasStationController.class).slash("gas-price").slash("current").withSelfRel();

        return new EntityModel<>(currentPetrolPrice).add(self);
    }
}
