package com.splitoil.car.domain;

import com.splitoil.car.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Transactional
@AllArgsConstructor
public class CarFacade {

    private CarCreator carCreator;

    private CarsRepository carsRepository;

    private CarCostRepository carCostRepository;

    private CarRefuelRepository carRefuelRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CarOutputDto addNewCarToCollection(final @NonNull AddCarDto addCarDto) {
        final Driver driver = carCreator.createDriver(addCarDto.getDriver());
        final Car car = carCreator.createCar(addCarDto);

        final List<Car> userCars = carsRepository.findAllByOwner(driver);
        if (userCars.size() > 20) {
            throw new IllegalStateException("User cannot have more than 20 cars");
        }

        carsRepository.save(car);

        return car.toDto();
    }

    public List<CarView> getAllCars(final @NonNull Long driverId) {
        final Driver driver = carCreator.createDriver(DriverDto.of(driverId));
        return carsRepository.findAllByOwnerView(driver);
    }

    public void deleteCar(final @NonNull Long carId) {
        final Car carToDelete = getCarByIdOrThrow(carId);
        carsRepository.delete(carToDelete);
    }

    public CarOutputDto editCarsFuelTank(final @NonNull FuelTankDto fuelTankDto) {
        final FuelTank fuelTank = carCreator.createFuelTank(fuelTankDto);
        final Car car = getCarByIdOrThrow(fuelTankDto.getCarId());
        car.addFuelTank(fuelTank);

        return car.toDto();
    }

    public CarOutputDto setCarsInitialMileage(final @NonNull AddCarMileageDto addCarMileageDto) {
        final Car car = getCarByIdOrThrow(addCarMileageDto.getCarId());
        car.setCarsInitialMileage(addCarMileageDto.getMileage());

        return car.toDto();
    }

    public CarOutputDto editCarsAverageFuelConsumptionManually(final @NonNull AddCarAverageFuelConsumptionDto fuelConsumptionDto) {
        final Car car = getCarByIdOrThrow(fuelConsumptionDto.getCarId());
        car.setAverageFuelConsumption(fuelConsumptionDto.getAvgFuelConsumption());

        return car.toDto();
    }

    private Car getCarByIdOrThrow(final @NonNull Long carId) {
        return carsRepository.findById(carId).orElseThrow(() -> new EntityNotFoundException("No car found"));
    }

    private void checkCarExistsOrThrow(final @NonNull Long carId) {
        carsRepository.findById(carId).ifPresentOrElse(e -> {
        }, () -> {
            throw new EntityNotFoundException("No car found");
        });
    }

    public void addCarCost(final @NonNull AddCarCostDto addCarCostDto) {
        checkCarExistsOrThrow(addCarCostDto.getCarId());
        final CarCost carCost = carCreator.createCarCost(addCarCostDto);
        carCostRepository.save(carCost);
    }

    public BigDecimal getTotalCarCostsSum(final Long carId) {
        return carCostRepository.getSumCostForCarId(carId);
    }

    public void addCarRefuel(final @NonNull RefuelCarDto refuelCarDto) {
        checkCarExistsOrThrow(refuelCarDto.getCarId());
        final CarRefuel carRefuel = carCreator.createCarRefuel(refuelCarDto);
        carRefuelRepository.save(carRefuel);
    }

    public Page<RefuelCarOutputDto> getRefuels(final Long carId, final Pageable page) {
        return carRefuelRepository.getRefuels(carId, page);
    }

    public CarFullDto getOneCar(final Long carId) {
        return getCarByIdOrThrow(carId).toFullDto();
    }

    public void handleTravelEnded(final TravelEndedEvent travelEndedEvent) {
        final Car car = getCarByIdOrThrow(travelEndedEvent.getCarId());
        car.addTravelInfo(travelEndedEvent);

        if (car.isAbleToCalculateAverages()) {
            //TODO: another event that new travel has benn processed. Should trigger the calculation
            //TODO: to service
            final BigDecimal totalRefuelCost = carRefuelRepository.getTotalRefuelCostForCar(car.getId());
            final BigDecimal totalRefuelAmountInLitres = carRefuelRepository.getTotalRefuelAmountInLitres(car.getId());
            car.calculateAvg1kmCost(totalRefuelCost);
            car.calculateAvgFuelConsumption(totalRefuelAmountInLitres);
        }
    }
}
