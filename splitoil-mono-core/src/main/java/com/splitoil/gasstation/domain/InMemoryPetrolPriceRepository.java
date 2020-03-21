package com.splitoil.gasstation.domain;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

class InMemoryPetrolPriceRepository implements PetrolPriceRepository {

    private final Map<Long, PetrolPrice> map = new HashMap<>();

    @SneakyThrows
    @Override
    public PetrolPrice save(final PetrolPrice entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);

        map.put(entity.getId(), entity);
        return entity;
    }

    @Override public <S extends PetrolPrice> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override public Optional<PetrolPrice> findById(final Long aLong) {
        return Optional.empty();
    }

    @Override public boolean existsById(final Long aLong) {
        return false;
    }

    @Override public Iterable<PetrolPrice> findAll() {
        return null;
    }

    @Override public Iterable<PetrolPrice> findAllById(final Iterable<Long> longs) {
        return null;
    }

    @Override public long count() {
        return 0;
    }

    @Override public void deleteById(final Long aLong) {

    }

    @Override public void delete(final PetrolPrice entity) {

    }

    @Override public void deleteAll(final Iterable<? extends PetrolPrice> entities) {

    }

    @Override public void deleteAll() {

    }

    @Override public Optional<PetrolPrice> findByAggregateId(final UUID uuid) {
        return map.values().stream().filter(e -> uuid.equals(e.getAggregateId())).findFirst();
    }

    @Override
    public Page<PetrolPrice> getLatestPrice(final GasStationId gasStationId, final PetrolType petrolType, final com.splitoil.shared.model.Currency currency, final Pageable pageable) {
        final PetrolPrice petrolPrice = map.values().stream()
            .filter(PetrolPrice::isAccepted)
            .filter((v -> v.isInThisPlace(gasStationId)))
            .filter(v -> v.isOfCurrency(currency))
            .filter(v -> v.isOfPetrolType(petrolType))
            .findFirst().orElse(null);

        return new PageImpl<>(Objects.isNull(petrolPrice) ? List.of() : List.of(petrolPrice));
    }

    @Override public Stream<PetrolPrice> findAllByGasStationIdAndCurrencyOrderByCreatedDesc(final GasStationId gasStationId, final com.splitoil.shared.model.Currency currency) {
        return map.values().stream()//.sorted(Comparator.comparing(p -> p.getCreated())) ???
            .filter(PetrolPrice::isAccepted)
            .filter((v -> v.isInThisPlace(gasStationId)))
            .filter(v -> v.isOfCurrency(currency))
            ;
    }
}
