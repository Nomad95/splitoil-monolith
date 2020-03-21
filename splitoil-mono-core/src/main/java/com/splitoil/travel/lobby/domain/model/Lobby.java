package com.splitoil.travel.lobby.domain.model;

import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractAggregateRoot;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.web.dto.LobbyOutputDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Lobby extends AbstractAggregateRoot {

    private static final BigDecimal NO_MAX_RATE = BigDecimal.ZERO;

    enum LobbyStatus {
        IN_CREATION,
        IN_CONFIGURATION,
        IN_TRAVEL,
        ENDED;

    }
    @NotBlank
    private String name;

    @NonNull
    private Driver lobbyCreator;//TODO change to lobby creator VO

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private LobbyStatus lobbyStatus;

    @NotNull
    private BigDecimal topRatePer1km;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Currency travelCurrency;

    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.LIST, value = "java.util.List"),
              @org.hibernate.annotations.Parameter(name = JsonUserType.ELEM_TYPE, value = "com.splitoil.travel.lobby.domain.model.CarId")
          })
    private List<CarId> cars;

    Lobby(final String name, final Driver lobbyCreator, final Currency currency) {
        this.lobbyCreator = lobbyCreator;
        this.name = name;
        this.lobbyStatus = LobbyStatus.IN_CREATION;
        this.topRatePer1km = NO_MAX_RATE;
        this.travelCurrency = currency;
        cars = new ArrayList<>();
    }

    public LobbyOutputDto toDto() {
        return LobbyOutputDto.builder()
            .lobbyId(getAggregateId())
            .lobbyStatus(lobbyStatus.name())
            .topRatePer1km(topRatePer1km)
            .travelCurrency(travelCurrency.name())
            .build();
    }

    public void addCar(final CarId car) {
        if (cars.isEmpty()) {
            lobbyStatus = LobbyStatus.IN_CONFIGURATION;
        }
        cars.add(car);
    }

    public void setTravelTopRatePer1km(final @NonNull BigDecimal rate) {
        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Top rate per 1 km should be positive");
        }
        if (lobbyStatus != LobbyStatus.IN_CONFIGURATION) {
            throw new IllegalStateException("Cant configure top rate per 1 km at this stage");
        }
        topRatePer1km = rate;
    }

    public void changeDefaultCurrency(final Currency currency) {
        if (lobbyStatus != LobbyStatus.IN_CONFIGURATION) {
            throw new IllegalStateException("Cant configure default currency at this stage");
        }
        travelCurrency = currency;
    }
}


