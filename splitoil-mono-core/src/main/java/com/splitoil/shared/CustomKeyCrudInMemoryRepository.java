package com.splitoil.shared;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomKeyCrudInMemoryRepository<K, T extends AbstractEntity > extends CrudInMemoryRepository<T> {

    protected Map<K, T> map = new HashMap<>();

    @SneakyThrows
    public <S extends T> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        putElementToMap(entity);
        return entity;
    }

    protected abstract <S extends T> void putElementToMap(final S entity);

    @Override
    public void delete(final T entity) {
        final K key = map.entrySet().stream().filter(entry -> entry.getValue().equals(entity)).map(entry -> entry.getKey()).findFirst().get();
        map.remove(key);
    }

}
