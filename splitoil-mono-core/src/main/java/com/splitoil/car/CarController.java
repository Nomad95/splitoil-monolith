package com.splitoil.car;

import com.splitoil.car.domain.CarFacade;
import com.splitoil.car.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarFacade carFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<CarOutputDto> addNewCar(@RequestBody @Valid @NonNull final AddCarDto addCarDto) {
        final CarOutputDto carOutputDto = carFacade.addNewCarToCollection(addCarDto);

        final Link getAll = linkTo(methodOn(CarController.class).getAllDriverCars(addCarDto.getDriver().getId())).withRel("getAll");
        final Link self = linkTo(CarController.class).withSelfRel();
        final Link addFuelTank = linkTo(CarController.class).slash("fuel-tank").withRel("addFuelTank");
        final Link addInitialMileage = linkTo(CarController.class).slash("initial-mileage").withRel("addInitialMileage");
        final Link addAvgFuelConsumpt = linkTo(CarController.class).slash("avg-fuel-consumption").withRel("avgFuelConsumption");

        return new EntityModel<>(carOutputDto)
            .add(self)
            .add(getAll)
            .add(addAvgFuelConsumpt)
            .add(addFuelTank)
            .add(addInitialMileage);
    }

    @GetMapping("/driver/{id}")
    public CollectionModel<EntityModel<CarView>> getAllDriverCars(@PathVariable Long id) {
        final List<CarView> allCars = carFacade.getAllCars(id);
        final Link self = linkTo(CarController.class).withSelfRel();

        return CollectionModel.wrap(allCars).add(self);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carFacade.deleteCar(id);
    }

    @PostMapping("/fuel-tank")
    public EntityModel<CarOutputDto> defineCarsFuelTank(@RequestBody @Valid @NonNull final FuelTankDto fuelTankDto) {
        final CarOutputDto carOutputDto = carFacade.editCarsFuelTank(fuelTankDto);

        final Link getAll = linkTo(methodOn(CarController.class).getAllDriverCars(carOutputDto.getDriver().getId())).withRel("getAll");
        final Link self = linkTo(CarController.class).slash("fuel-tank").withSelfRel();

        return new EntityModel<>(carOutputDto).add(self).add(getAll);
    }

    @PostMapping("/initial-mileage")
    public EntityModel<CarOutputDto> defineCarsInitialMileage(@RequestBody @Valid @NonNull final AddCarMileageDto addCarMileageDto) {
        final CarOutputDto carOutputDto = carFacade.setCarsInitialMileage(addCarMileageDto);

        final Link getAll = linkTo(methodOn(CarController.class).getAllDriverCars(carOutputDto.getDriver().getId())).withRel("getAll");
        final Link self = linkTo(CarController.class).slash("initial-mileage").withSelfRel();

        return new EntityModel<>(carOutputDto).add(self).add(getAll);
    }

    @PostMapping("/avg-fuel-consumption")
    public EntityModel<CarOutputDto> defineCarsAverageFuelConsumptionManually(@RequestBody @Valid @NonNull final AddCarAverageFuelConsumptionDto consumptionDto) {
        final CarOutputDto carOutputDto = carFacade.editCarsAverageFuelConsumptionManually(consumptionDto);

        final Link getAll = linkTo(methodOn(CarController.class).getAllDriverCars(carOutputDto.getDriver().getId())).withRel("getAll");
        final Link self = linkTo(CarController.class).slash("avg-fuel-consumption").withSelfRel();

        return new EntityModel<>(carOutputDto).add(self).add(getAll);
    }

    @PostMapping("/cost")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCarCost(@RequestBody @Valid @NonNull final AddCarCostDto costDto) {
        carFacade.addCarCost(costDto);
    }

    @GetMapping("/{id}/cost")
    public BigDecimal getCarOverallCostSum(@PathVariable Long id) {
        return carFacade.getTotalCarCostsSum(id);
    }

    @PostMapping("/refuel")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRefuelToCar(@RequestBody @Valid @NonNull final RefuelCarDto refuelCarDto) {
        carFacade.addCarRefuel(refuelCarDto);
    }

    @GetMapping("/{id}/refuel")
    public Page<RefuelCarOutputDto> getCarRefuels(@PathVariable @NonNull Long id, Pageable page) {
        return carFacade.getRefuels(id, page);
    }

    @GetMapping("/{id}")
    public CarFullDto getOneCar(@PathVariable Long id) {
        return carFacade.getOneCar(id);
    }
}
