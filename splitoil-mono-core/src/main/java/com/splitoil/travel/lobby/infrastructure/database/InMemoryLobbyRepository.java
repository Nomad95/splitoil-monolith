package com.splitoil.travel.lobby.infrastructure.database;

import com.splitoil.travel.lobby.application.dto.ParticipantIdView;
import com.splitoil.travel.lobby.domain.model.Lobby;
import com.splitoil.travel.lobby.domain.model.LobbyRepository;
import com.splitoil.travel.lobby.web.dto.LobbyParticipantDto;
import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryLobbyRepository implements LobbyRepository {

    private Map<Long, Lobby> map = new HashMap<>();

    @Override
    @SneakyThrows
    public <S extends Lobby> S save(final S entity) {
        final long id = map.size() + 1;

        final Field id1 = entity.getClass().getSuperclass().getDeclaredField("id");
        id1.setAccessible(true);

        ReflectionUtils.setField(id1, entity, id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public <S extends Lobby> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Lobby> findById(final Long aLong) {
        return Optional.ofNullable(map.get(aLong));
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public Iterable<Lobby> findAll() {
        return null;
    }

    @Override
    public Iterable<Lobby> findAllById(final Iterable<Long> longs) {
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
    public void delete(final Lobby entity) {
        final Long key = map.entrySet().stream()
            .filter(entry -> entry.getValue().equals(entity))
            .map(entry -> entry.getKey())
            .findFirst()
            .get();
        map.remove(key);
    }

    @Override
    public void deleteAll(final Iterable<? extends Lobby> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override public Optional<Lobby> findByAggregateId(final UUID uuid) {
        return map.values().stream().filter(e -> uuid.equals(e.getAggregateId())).findFirst();
    }

    @Override public List<ParticipantIdView> getLobbyPassengersView(final UUID lobbyId) {
        final Optional<Lobby> any = map.values().stream().filter(lobby -> lobby.getAggregateId().equals(lobbyId)).findAny();
        if (any.isEmpty()) {
            return List.of();
        }

        return any.get().toDto().getParticipants().stream()
            .map(LobbyParticipantDto::getUserId)
            .map(ParticipantIdView::new)
            .collect(Collectors.toUnmodifiableList());
    }
}
