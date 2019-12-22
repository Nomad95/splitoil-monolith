package com.splitoil.gasstation;

import com.splitoil.gasstation.domain.GasStationsFacade;
import com.splitoil.gasstation.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/gas-station")
class GasStationController {

    private GasStationsFacade gasStationsFacade;

    @PostMapping(value = "/observe")
    public void addGasStationToObservable(@RequestBody @NonNull @Valid final AddToObservableDto command) {
        gasStationsFacade.addToObservables(command);
    }

    @GetMapping(value = "/observe/driver/{driverId}")
    public List<GasStationIdDto> getObservedGasStations(final @NonNull @PathVariable Long driverId) {
        return gasStationsFacade.getObservedGasStations(driverId);
    }

    @PostMapping(value = "/rate")
    public void rateGasStation(@RequestBody @NonNull @Valid final AddRatingDto command) {
        gasStationsFacade.rateGasStation(command);
    }

    @PostMapping(value = "/rating")
    public BigDecimal getGasStationRating(final @RequestBody GasStationIdDto gasStationIdDto) {
        return gasStationsFacade.getRating(gasStationIdDto);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/gas-price")
    public UUID addPetrolPriceToGasStation(final @RequestBody AddPetrolPriceDto addPetrolPriceDto) {
        return gasStationsFacade.addPetrolPrice(addPetrolPriceDto);
    }

    @PostMapping(value = "/gas-price/accept")
    public void acceptGasStationPrice(final @RequestBody AcceptPetrolPriceDto addPetrolPriceDto) {
        gasStationsFacade.acceptPetrolPrice(addPetrolPriceDto);
    }
}
