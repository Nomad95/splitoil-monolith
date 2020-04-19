package com.splitoil.travel.lobby.domain.model;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@Access(AccessType.FIELD)
@EqualsAndHashCode(callSuper = false, of = {"carId", "driverId"})
@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Car implements Serializable {
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

    boolean isEmpty() {
        return seatsOccupied == 0;
    }

    void occupySeat() {
        if (isFull()) {
            throw new IllegalStateException("Cannot occupy another seat. Car is full");
        }

        seatsOccupied++;
    }

    void disoccupySeat() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot disoccupy seat. Empty");
        }

        seatsOccupied--;
    }
}
