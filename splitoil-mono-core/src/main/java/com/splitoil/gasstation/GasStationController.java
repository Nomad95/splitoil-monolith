package com.splitoil.gasstation;

import com.splitoil.gasstation.domain.GasStationsFacade;
import com.splitoil.gasstation.dto.AddGasStationToObservableInputDto;
import com.splitoil.gasstation.dto.ObservedGasStationOutput;
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
    private void addGasStationToObservable(@RequestBody @NonNull @Valid final AddGasStationToObservableInputDto command) {
        gasStationsFacade.addToObservables(command);
    }

    @GetMapping(value = "/observe/driver/{driverId}")
    private List<ObservedGasStationOutput> getObservedGasStations(final @NonNull @PathVariable Long driverId) {
        return gasStationsFacade.getObservedGasStations(driverId);
    }
}
