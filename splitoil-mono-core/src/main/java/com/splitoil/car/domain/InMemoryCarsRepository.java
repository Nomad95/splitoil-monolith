package com.splitoil.car.domain;

import com.splitoil.car.dto.CarView;
import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class InMemoryCarsRepository implements CarsRepository {

    private Map<Long, Car> map = new HashMap<>();

    @Override
    public List<Car> findAllByOwner(final Driver owner) {
        return map.values().stream().filter(c -> c.getOwner().equals(owner)).collect(Collectors.toList());
    }

    @Override
    public List<CarView> findAllByOwnerView(final Driver owner) {
        return map.entrySet().stream().filter(c -> c.getValue().getOwner().equals(owner))
            .map(c ->
                CarView.builder().brand(c.getValue().getBrand()).name(c.getValue().getName()).driverId(c.getValue().getOwner().getDriverId()).id(c.getKey()).build()
            )
            .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public <S extends Car> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);
        return entity;
    }

    @Override public <S extends Car> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override public Optional<Car> findById(final Long aLong) {
        return Optional.ofNullable(map.get(aLong));
    }

    @Override public boolean existsById(final Long aLong) {
        return false;
    }

    @Override public Iterable<Car> findAll() {
        return null;
    }

    @Override public Iterable<Car> findAllById(final Iterable<Long> longs) {
        return null;
    }

    @Override public long count() {
        return 0;
    }

    @Override public void deleteById(final Long aLong) {

    }

    @Override public void delete(final Car entity) {
        final Long key = map.entrySet().stream().filter(entry -> entry.getValue().equals(entity)).map(entry -> entry.getKey()).findFirst().get();
        map.remove(key);
    }

    @Override public void deleteAll(final Iterable<? extends Car> entities) {

    }

    @Override public void deleteAll() {

    }
}
