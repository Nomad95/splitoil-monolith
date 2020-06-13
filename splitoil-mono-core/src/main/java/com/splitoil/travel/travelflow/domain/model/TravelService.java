package com.splitoil.travel.travelflow.domain.model;

import com.splitoil.shared.annotation.DomainService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@DomainService
@AllArgsConstructor
public class TravelService {

    //TODO: spr które operacje w encjach nie są NATURALNE dla obiektu i wydziel do tego
    public boolean hasAllCarsStateSet(final @NonNull Travel travel, final @NonNull List<UUID> carsIds) {
        final List<UUID> travelCars = travel.getInitialState().getStatedCars();
        for (final UUID id : carsIds) {
            if (!travelCars.contains(id)) {
                return false;
            }
        }

        return true;
    }
}
