package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false, of = {"carId", "driverId"})
@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Car extends AbstractValue {
    @NonNull private CarId carId;
    @NonNull private UUID driverId;
    private int numberOfSeats;
    private int seatsOccupied;

    static Car withDriver(final @NonNull CarId carId, @NonNull final UUID driverId, final int numberOfSeats) {
        if (numberOfSeats <= 0) {
            throw new IllegalArgumentException("Number of seats should be positive");
        }

        return Car.of(carId, driverId, numberOfSeats, 1);
    }

    boolean isFull() {
        return seatsOccupied == numberOfSeats;
    }

    Car occupySeat() {
        if (isFull()) {
            throw new IllegalStateException("Cannot occupy another seat. Car is full");
        }

        return Car.of(carId, driverId, numberOfSeats, seatsOccupied + 1);
    }
}
