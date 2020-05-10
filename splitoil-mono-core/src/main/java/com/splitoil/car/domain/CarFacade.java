package com.splitoil.car.domain;

import com.splitoil.car.domain.event.CarAddedToCollection;
import com.splitoil.car.domain.event.CarDeleted;
import com.splitoil.car.dto.*;
import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.event.EventPublisher;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Transactional
@ApplicationService
@AllArgsConstructor
public class CarFacade {

    private CarCreator carCreator;

    private CarsRepository carsRepository;

    private CarCostRepository carCostRepository;

    private CarRefuelRepository carRefuelRepository;

    private EventPublisher eventPublisher;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CarOutputDto addNewCarToCollection(final @NonNull AddCarDto addCarDto) {
        final Driver driver = carCreator.createDriver(addCarDto.getDriver());
        final Car car = carCreator.createCar(addCarDto);

        final List<Car> userCars = carsRepository.findAllByOwner(driver);
        if (userCars.size() > 20) {
            throw new IllegalStateException("User cannot have more than 20 cars");
        }

        carsRepository.save(car);
        eventPublisher.publish(new CarAddedToCollection(car.getAggregateId(), driver.getDriverId()));

        return car.toDto();
    }

    public List<CarView> getAllCars(final @NonNull UUID driverId) {
        final Driver driver = carCreator.createDriver(DriverDto.of(driverId));
        return carsRepository.findAllByOwnerView(driver);
    }

    public void deleteCar(final @NonNull UUID carId) {
        final Car carToDelete = carsRepository.getOneByAggregateId(carId);
        carsRepository.delete(carToDelete);

        eventPublisher.publish(new CarDeleted(carToDelete.getAggregateId()));
    }

    public CarOutputDto editCarsFuelTank(final @NonNull FuelTankDto fuelTankDto) {
        final FuelTank fuelTank = carCreator.createFuelTank(fuelTankDto);
        final Car car = carsRepository.getOneByAggregateId(fuelTankDto.getCarId());
        car.addFuelTank(fuelTank);

        return car.toDto();
    }

    public CarOutputDto setCarsInitialMileage(final @NonNull AddCarMileageDto addCarMileageDto) {
        final Car car = carsRepository.getOneByAggregateId(addCarMileageDto.getCarId());
        car.setCarsInitialMileage(addCarMileageDto.getMileage());

        return car.toDto();
    }

    public CarOutputDto editCarsAverageFuelConsumptionManually(final @NonNull AddCarAverageFuelConsumptionDto fuelConsumptionDto) {
        final Car car = carsRepository.getOneByAggregateId(fuelConsumptionDto.getCarId());
        car.setAverageFuelConsumption(fuelConsumptionDto.getAvgFuelConsumption());

        return car.toDto();
    }

    private void checkCarExistsOrThrow(final @NonNull UUID carId) {
        carsRepository.findByAggregateId(carId).ifPresentOrElse(e -> {
        }, () -> {
            throw new EntityNotFoundException("No car found");
        });
    }

    public CarCostDto addCarCost(final @NonNull AddCarCostDto addCarCostDto) {
        checkCarExistsOrThrow(addCarCostDto.getCarId());
        final CarCost carCost = carCreator.createCarCost(addCarCostDto);
        carCostRepository.save(carCost);

        return getCarCostDetails(carCost.getAggregateId());
    }

    public BigDecimal getTotalCarCostsSum(final @NonNull UUID carId) {
        return carCostRepository.getSumCostForCarId(carId);
    }

    public CarCostDto getCarCostDetails(final @NonNull UUID carCostId) {
        return carCostRepository.getCarCostDto(carCostId);
    }

    public void addCarRefuel(final @NonNull RefuelCarDto refuelCarDto) {
        checkCarExistsOrThrow(refuelCarDto.getCarId());
        final CarRefuel carRefuel = carCreator.createCarRefuel(refuelCarDto);
        carRefuelRepository.save(carRefuel);
    }

    public Page<RefuelCarOutputDto> getRefuels(final UUID carId, final Pageable page) {
        return carRefuelRepository.getRefuels(carId, page);
    }

    public CarFullDto getOneCar(final UUID carId) {
        return carsRepository.getOneByAggregateId(carId).toFullDto();
    }

    public void handleTravelEnded(final TravelEndedEvent travelEndedEvent) {
        final Car car = carsRepository.getOneByAggregateId(travelEndedEvent.getCarId());
        car.addTravelInfo(travelEndedEvent);

        if (car.isAbleToCalculateAverages()) {
            //TODO: another event that new travel has been processed. Should trigger the calculation
            final BigDecimal totalRefuelCost = carRefuelRepository.getTotalRefuelCostForCar(car.getAggregateId());
            final BigDecimal totalRefuelAmountInLitres = carRefuelRepository.getTotalRefuelAmountInLitres(car.getAggregateId());
            car.calculateAvg1kmCost(totalRefuelCost);
            car.calculateAvgFuelConsumption(totalRefuelAmountInLitres);
        }
    }
}
