package com.splitoil.car.domain;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryCarCostRepository implements CarCostRepository {

    private Map<Long, CarCost> map = new HashMap<>();

    @Override
    @SneakyThrows
    public <S extends CarCost> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public <S extends CarCost> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CarCost> findById(final Long aLong) {
        return Optional.ofNullable(map.get(aLong));
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public Iterable<CarCost> findAll() {
        return null;
    }

    @Override
    public Iterable<CarCost> findAllById(final Iterable<Long> longs) {
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
    public void delete(final CarCost entity) {
        final Long key = map.entrySet().stream().filter(entry -> entry.getValue().equals(entity)).map(entry -> entry.getKey()).findFirst().get();
        map.remove(key);
    }

    @Override
    public void deleteAll(final Iterable<? extends CarCost> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public BigDecimal getSumCostForCarId(final Long carId) {
        return map.values().stream()
            .filter(v -> v.getCarId().equals(carId))
            .map(CarCost::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
