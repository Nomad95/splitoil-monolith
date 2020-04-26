package com.splitoil.travel.travelflow.infrastructure.database;

import com.splitoil.travel.travelflow.domain.model.Travel;
import com.splitoil.travel.travelflow.domain.model.TravelRepository;
import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryTravelRepository implements TravelRepository {

    private final Map<Long, Travel> map = new HashMap<>();

    @Override
    @SneakyThrows
    public <S extends Travel> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public <S extends Travel> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Travel> findById(final Long aLong) {
        return Optional.ofNullable(map.get(aLong));
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public Iterable<Travel> findAll() {
        return null;
    }

    @Override
    public Iterable<Travel> findAllById(final Iterable<Long> longs) {
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
    public void delete(final Travel entity) {
        final Long key = map.entrySet().stream()
            .filter(entry -> entry.getValue().equals(entity))
            .map(entry -> entry.getKey())
            .findFirst()
            .get();
        map.remove(key);
    }

    @Override
    public void deleteAll(final Iterable<? extends Travel> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override public Optional<Travel> findByAggregateId(final UUID uuid) {
        return map.values().stream().filter(e -> uuid.equals(e.getAggregateId())).findFirst();
    }
}
