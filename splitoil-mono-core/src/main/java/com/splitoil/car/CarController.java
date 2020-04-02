package com.splitoil.car;

import com.splitoil.car.domain.CarFacade;
import com.splitoil.car.dto.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
        final Link addCarCost = linkTo(CarController.class).slash("cost").withRel("addCarCost");
        final Link addCarRefuel = linkTo(CarController.class).slash("refuel").withRel("addCarRefuel");
        final Link getOne = linkTo(CarController.class).slash(carOutputDto.getId()).withRel("getOne");

        return new EntityModel<>(carOutputDto)
            .add(self)
            .add(getAll)
            .add(addAvgFuelConsumpt)
            .add(addFuelTank)
            .add(addInitialMileage)
            .add(addCarCost)
            .add(addCarRefuel)
            .add(getOne);
    }

    @GetMapping("/driver/{id}")
    public CollectionModel<EntityModel<CarView>> getAllDriverCars(@PathVariable UUID id) {
        final List<CarView> allCars = carFacade.getAllCars(id);
        final Link self = linkTo(CarController.class).slash("driver").slash(id).withSelfRel();

        return CollectionModel.wrap(allCars).add(self);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable UUID id) {
        carFacade.deleteCar(id);
    }

    @PostMapping("/fuel-tank")
    public EntityModel<CarOutputDto> defineCarsFuelTank(@RequestBody @Valid @NonNull final FuelTankDto fuelTankDto) {
        final CarOutputDto carOutputDto = carFacade.editCarsFuelTank(fuelTankDto);

        final Link getOne = linkTo(CarController.class).slash(carOutputDto.getId()).withRel("getOne");
        final Link self = linkTo(CarController.class).slash("fuel-tank").withSelfRel();

        return new EntityModel<>(carOutputDto)
            .add(self)
            .add(getOne);
    }

    @PostMapping("/initial-mileage")
    public EntityModel<CarOutputDto> defineCarsInitialMileage(@RequestBody @Valid @NonNull final AddCarMileageDto addCarMileageDto) {
        final CarOutputDto carOutputDto = carFacade.setCarsInitialMileage(addCarMileageDto);

        final Link getOne = linkTo(CarController.class).slash(carOutputDto.getId()).withRel("getOne");
        final Link self = linkTo(CarController.class).slash("initial-mileage").withSelfRel();

        return new EntityModel<>(carOutputDto)
            .add(self)
            .add(getOne);
    }

    @PostMapping("/avg-fuel-consumption")
    public EntityModel<CarOutputDto> defineCarsAverageFuelConsumptionManually(@RequestBody @Valid @NonNull final AddCarAverageFuelConsumptionDto consumptionDto) {
        final CarOutputDto carOutputDto = carFacade.editCarsAverageFuelConsumptionManually(consumptionDto);

        final Link getOne = linkTo(CarController.class).slash(carOutputDto.getId()).withRel("getOne");
        final Link self = linkTo(CarController.class).slash("avg-fuel-consumption").withSelfRel();

        return new EntityModel<>(carOutputDto)
            .add(self)
            .add(getOne);
    }

    @PostMapping("/cost")
    @ResponseStatus(HttpStatus.CREATED)
    public Links addCarCost(@RequestBody @Valid @NonNull final AddCarCostDto costDto) {
        carFacade.addCarCost(costDto);

        final Link getOne = linkTo(CarController.class).slash(costDto.getCarId()).withRel("getOne");
        final Link getOverallCosts = linkTo(CarController.class).slash(costDto.getCarId()).slash("cost").withRel("getOverallCosts");
        final Link addCarCost = linkTo(CarController.class).slash("cost").withSelfRel();

        return Links.of(getOne, getOverallCosts, addCarCost);
    }

    @GetMapping("/{id}/cost")
    public EntityModel<BigDecimal> getCarOverallCostSum(@PathVariable UUID id) {
        final BigDecimal totalCarCostsSum = carFacade.getTotalCarCostsSum(id);

        final Link getOne = linkTo(CarController.class).slash(id).withRel("getOne");
        final Link getOverallCosts = linkTo(CarController.class).slash(id).slash("cost").withSelfRel();
        final Link addCarCost = linkTo(CarController.class).slash("cost").withRel("addCarCost");

        return new EntityModel<>(totalCarCostsSum)
            .add(getOne)
            .add(getOverallCosts)
            .add(addCarCost);
    }

    @PostMapping("/refuel")
    @ResponseStatus(HttpStatus.CREATED)
    public Links addRefuelToCar(@RequestBody @Valid @NonNull final RefuelCarDto refuelCarDto) {
        carFacade.addCarRefuel(refuelCarDto);

        final Link getOne = linkTo(CarController.class).slash(refuelCarDto.getCarId()).withRel("getOne");
        final Link getCarRefuels = linkTo(CarController.class).slash(refuelCarDto.getCarId()).slash("refuel").withRel("getCarRefuels");
        final Link addCarRefuel = linkTo(CarController.class).slash("refuel").withSelfRel();

        return Links.of(getOne, getCarRefuels, addCarRefuel);
    }

    @GetMapping("/{id}/refuel")
    public CollectionModel<EntityModel<RefuelCarOutputDto>> getCarRefuels(@PathVariable @NonNull UUID id, Pageable page) {
        final Page<RefuelCarOutputDto> refuels = carFacade.getRefuels(id, page);

        final Link getOne = linkTo(CarController.class).slash(id).withRel("getOne");
        final Link getCarRefuels = linkTo(CarController.class).slash(id).slash("refuel").withSelfRel();
        final Link addCarRefuel = linkTo(CarController.class).slash("refuel").withRel("addCarRefuel");

        final PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(refuels.getSize(), refuels.getNumber(), refuels.getTotalElements());
        return PagedModel.wrap(refuels.getContent(), pageMetadata)
            .add(getOne)
            .add(getCarRefuels)
            .add(addCarRefuel);
    }

    @GetMapping("/{id}")
    public CarFullDto getOneCar(@PathVariable UUID id) {
        return carFacade.getOneCar(id);
    }
}
