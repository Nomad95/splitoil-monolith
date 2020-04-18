package com.splitoil.travel.lobby.domain.model;

import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractEntity;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.web.dto.LobbyOutputDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter//TODO: tylko package
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Lobby extends AbstractEntity {

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
    private Driver lobbyCreator;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private LobbyStatus lobbyStatus;

    @NotNull
    private BigDecimal topRatePer1km;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Currency travelCurrency;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER,
        mappedBy = "lobby"
    )
    private List<TravelParticipant> participants;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.travel.lobby.domain.model.TravelCars"),
          })
    private TravelCars cars;

    Lobby(final @NonNull String name, final @NonNull Driver lobbyCreator, final @NonNull Currency currency) {
        this.lobbyCreator = lobbyCreator;
        this.name = name;
        this.lobbyStatus = LobbyStatus.IN_CREATION;
        this.topRatePer1km = NO_MAX_RATE;
        this.travelCurrency = currency;
        cars = TravelCars.empty();
        participants = new ArrayList<>();
    }

    public LobbyOutputDto toDto() {
        return LobbyOutputDto.builder()
            .lobbyId(getAggregateId())
            .lobbyStatus(lobbyStatus.name())
            .topRatePer1km(topRatePer1km)
            .travelCurrency(travelCurrency.name())
            .participants(participants.stream().map(TravelParticipant::toDto).collect(Collectors.toUnmodifiableList()))
            .cars(cars.toDtoList())
            .build();
    }

    public void addCar(final @NonNull Car car) {
        if (cars.hasNoCars()) {
            lobbyStatus = LobbyStatus.IN_CONFIGURATION;
        }
        if (cars.isPresent(car.getCarId())) {
            throw new IllegalStateException("Car already exists in this lobby");
        }

        cars.addCar(car);
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

    public void changeDefaultCurrency(final @NonNull Currency currency) {
        if (lobbyStatus != LobbyStatus.IN_CONFIGURATION) {
            throw new IllegalStateException("Cant configure default currency at this stage");
        }
        travelCurrency = currency;
    }

    public void addPassengerToCar(final @NonNull Participant travelParticipant, final @NonNull CarId carId) {
        if (lobbyStatus != LobbyStatus.IN_CONFIGURATION) {
            throw new IllegalStateException("Cant add passengers in this stage");
        }

        final TravelParticipant participant = TravelParticipant.builder()
            .displayName(travelParticipant.getDisplayName())
            .travelCurrency(travelParticipant.getTravelCurrency())
            .userId(travelParticipant.getParticipantId())
            .assignedCar(carId)
            .participantType(travelParticipant.getParticipantType())
            .lobby(this)
            .build();

        if (participants.contains(participant)) {
            throw new IllegalStateException(String.format("Passenger %s is already in this lobby", travelParticipant.getDisplayName()));
        }

        if (!cars.isPresent(carId)) {
            throw new IllegalStateException(String.format("Car %s doesn't exist in this lobby", carId.getCarId()));
        }

        final Car car = cars.getCar(carId);

        if (car.isFull()) {
            throw new IllegalStateException(String.format("Can't add another passenger to car %s. Car is full", carId.getCarId()));
        }

        cars.occupySeatOfCar(carId);
        participants.add(participant);
    }

    public void toggleCostCharging(final @NonNull UUID participantId) {
        final TravelParticipant participant = findParticipant(participantId);
        participant.toggleCostCharging();
    }

    public void changeParticipantTravelCurrency(final @NonNull UUID participantId, final @NonNull Currency currency) {
        findParticipant(participantId).changeTravelCurrency(currency);
    }

    public void assignParticipantToCar(final CarId car, final UUID participantId) {
        if (cars.isAbsent(car)) {
            throw new IllegalStateException("Car doesn't exist in this lobby");
        }

        final Car newCar = cars.getCar(car);
        if (newCar.isFull()) {
            throw new IllegalStateException("Cannot assign more passengers to car " + car.getCarId());
        }

        final TravelParticipant participant = findParticipant(participantId);

        final Car currentCar = cars.getCar(participant.getAssignedCar()); //TODO: to jakies dziewne
        cars.addCar(currentCar.disoccupySeat());

        participant.reseatTo(car);
    }

    private TravelParticipant findParticipant(final @NonNull UUID participantId) {
        return participants.stream()
            .filter(travelParticipant -> travelParticipant.hasUserId(participantId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Participant %s is not present in lobby %s", participantId, getAggregateId())));
    }
}


