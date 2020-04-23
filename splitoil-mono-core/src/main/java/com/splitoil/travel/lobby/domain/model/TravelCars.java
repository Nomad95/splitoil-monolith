package com.splitoil.travel.lobby.domain.model;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.travel.lobby.web.dto.CarDto;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    boolean isAbsent(final @NonNull CarId carId) {
        return !isPresent(carId);
    }

    void addCar(final @NonNull Car car) {
        cars.put(car.getCarId().getCarId(), car);
    }

    void occupySeatOfCar(final @NonNull CarId carId) {
        if (!isPresent(carId)) {
            throw new IllegalArgumentException(String.format("No such car - %s", carId.getCarId()));
        }

        final Car car = cars.get(carId.getCarId());
        car.occupySeat();
    }

    void disoccupySeatOfCar(final @NonNull CarId carId) {
        if (!isPresent(carId)) {
            throw new IllegalArgumentException(String.format("No such car - %s", carId.getCarId()));
        }

        final Car car = cars.get(carId.getCarId());
        car.disoccupySeat();
    }

    Car getCar(final @NonNull CarId carId) {
        if (!isPresent(carId)) {
            throw new IllegalArgumentException(String.format("No such car - %s", carId.getCarId()));
        }

        return cars.get(carId.getCarId());
    }

    //TODO: czy moze byc ref do DTOsow??
    List<CarDto> toDtoList() {
        return cars.values().stream()
            .map(car -> CarDto.builder().id(car.getCarId().getCarId()).seatsOccupied(car.getSeatsOccupied()).build())
            .collect(Collectors.toUnmodifiableList());
    }

    void removeCar(final CarId carId) {
        if (!isPresent(carId)) {
            throw new IllegalArgumentException(String.format("Cannot remove. No such car - %s", carId.getCarId()));
        }

        cars.remove(carId.getCarId());
    }

    int countCars() {
        return cars.size();
    }
}
