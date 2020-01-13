package com.splitoil.car.domain;

import com.splitoil.car.dto.RefuelCarOutputDto;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class InMemoryCarRefuelRepository implements CarRefuelRepository {

    private Map<Long, CarRefuel> map = new HashMap<>();

    @Override
    @SneakyThrows
    public <S extends CarRefuel> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public <S extends CarRefuel> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CarRefuel> findById(final Long aLong) {
        return Optional.ofNullable(map.get(aLong));
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public Iterable<CarRefuel> findAll() {
        return null;
    }

    @Override
    public Iterable<CarRefuel> findAllById(final Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final Long aLong) {

    }

    @Override
    public void delete(final CarRefuel entity) {
        final Long key = map.entrySet().stream().filter(entry -> entry.getValue().equals(entity)).map(entry -> entry.getKey()).findFirst().get();
        map.remove(key);
    }

    @Override
    public void deleteAll(final Iterable<? extends CarRefuel> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Page<RefuelCarOutputDto> getRefuels(final Long carId, final Pageable page) {
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
                .build())
            .collect(toList());
        return new PageImpl<>(view);
    }

    @Override
    public BigDecimal getTotalRefuelCostForCar(final Long carId) {
        return map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(CarRefuel::getValue)
            .reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }

    @Override public BigDecimal getTotalRefuelAmountInLitres(final Long carId) {
        return map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(CarRefuel::getFuelAmountInLitres)
            .reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }
}
