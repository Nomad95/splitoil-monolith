package com.splitoil.gasstation;

import com.splitoil.gasstation.domain.GasStationsFacade;
import com.splitoil.gasstation.dto.AddRatingDto;
import com.splitoil.gasstation.dto.AddToObservableDto;
import com.splitoil.gasstation.dto.GasStationIdDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/gas-station")
class GasStationController {

    private GasStationsFacade gasStationsFacade;

    @PostMapping(value = "/observe")
    private void addGasStationToObservable(@RequestBody @NonNull @Valid final AddToObservableDto command) {
        gasStationsFacade.addToObservables(command);
    }

    @GetMapping(value = "/observe/driver/{driverId}")
    private List<GasStationIdDto> getObservedGasStations(final @NonNull @PathVariable Long driverId) {
        return gasStationsFacade.getObservedGasStations(driverId);
    }

    @PostMapping(value = "/rate")
    private void rateGasStation(@RequestBody @NonNull @Valid final AddRatingDto command) {
        gasStationsFacade.rateGasStation(command);
    }
}
