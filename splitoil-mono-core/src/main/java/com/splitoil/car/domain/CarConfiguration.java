package com.splitoil.car.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CarConfiguration {

    CarFacade carFacade() {
        return new CarFacade(new CarCreator(), new InMemoryCarsRepository(), new InMemoryCarCostRepository(), new InMemoryCarRefuelRepository());
    }

    @Bean
    CarFacade carFacade(final CarsRepository carsRepository, final CarCostRepository carCostRepository, final CarRefuelRepository carRefuelRepository) {
        final CarCreator carCreator = new CarCreator();
        return new CarFacade(carCreator, carsRepository, carCostRepository, carRefuelRepository);
    }
}
