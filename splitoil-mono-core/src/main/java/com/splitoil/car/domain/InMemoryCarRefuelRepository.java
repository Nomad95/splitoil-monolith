package com.splitoil.car.domain;

import com.splitoil.car.dto.RefuelCarOutputDto;
import com.splitoil.shared.CrudInMemoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

class InMemoryCarRefuelRepository extends CrudInMemoryRepository<CarRefuel> implements CarRefuelRepository {

    @Override
    public Page<RefuelCarOutputDto> getRefuels(final UUID carId, final Pageable page) {
        final List<RefuelCarOutputDto> view = map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(v -> RefuelCarOutputDto.builder()
                .amount(v.getFuelAmountInLitres())
                .carId(v.getCarId())
                .cost(v.getValue())
                .date(v.getCostDate())
                .gasStationName(v.getGasStation().getName())
                .petrolType(v.getPetrolType()
                    .name())
                .currency(v.getCurrency().name())
                .build())
            .collect(toList());
        return new PageImpl<>(view);
    }

    @Override
    public BigDecimal getTotalRefuelCostForCar(final UUID carId) {
        return map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(CarRefuel::getValue)
            .reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }

    @Override public BigDecimal getTotalRefuelAmountInLitres(final UUID carId) {
        return map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(CarRefuel::getFuelAmountInLitres)
            .reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }
}
