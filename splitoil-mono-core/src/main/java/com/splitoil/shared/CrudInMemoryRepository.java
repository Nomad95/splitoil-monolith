package com.splitoil.shared;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CrudInMemoryRepository<T extends AbstractEntity> {

    protected Map<Long, T> map = new HashMap<>();

    @SneakyThrows
    public <S extends T> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);
        return entity;
    }

    public Optional<T> findByAggregateId(final UUID aggregateId) {
        return map.values().stream().filter(e -> e.getAggregateId().equals(aggregateId)).findFirst();
    }

    public <S extends T> Iterable<S> saveAll(final Iterable<S> entities) {
        for (final S entity : entities) {
            save(entity);
        }

        return null;
    }

    public Optional<T> findById(final Long aLong) {
        return Optional.ofNullable(map.get(aLong));
    }

    public boolean existsById(final Long aLong) {
        throw new RuntimeException("Not implemented");
    }

    public Iterable<T> findAll() {
        return map.values();
    }

    public Iterable<T> findAllById(final Iterable<Long> longs) {
        throw new RuntimeException("Not implemented");
    }

    public long count() {
        return map.values().size();
    }

    public void deleteById(final Long aLong) {
        throw new RuntimeException("Not implemented");
    }

    public void delete(final T entity) {
        final Long key = map.entrySet().stream().filter(entry -> entry.getValue().equals(entity)).map(entry -> entry.getKey()).findFirst().get();
        map.remove(key);
    }

    public void deleteAll(final Iterable<? extends T> entities) {
        for (final T entity : entities) {
            delete(entity);
        }
    }

    public void deleteAll() {
        map.clear();
    }

}
