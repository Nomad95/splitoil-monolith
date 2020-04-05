package com.splitoil.travel.lobby.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelCars implements JsonEntity, Serializable {

    private Map<UUID, Car> cars;

    static TravelCars empty() {
        return new TravelCars(new HashMap<>());
    }

    boolean hasNoCars() {
        return cars.isEmpty();
    }

    boolean isPresent(final @NonNull CarId carId) {
        return cars.containsKey(carId.getCarId());
    }

    void addCar(final @NonNull Car car) {
        cars.put(car.getCarId().getCarId(), car);
    }

    void occupySeatOfCar(final @NonNull CarId carId) {
        if (!isPresent(carId)) {
            throw new IllegalArgumentException(String.format("No such car - %s", carId.getCarId()));
        }

        final Car car = cars.get(carId.getCarId());
        cars.put(carId.getCarId(), car.occupySeat());
    }

    Car getCar(final @NonNull CarId carId) {
        if (!isPresent(carId)) {
            throw new IllegalArgumentException(String.format("No such car - %s", carId.getCarId()));
        }

        return cars.get(carId.getCarId());
    }

}
