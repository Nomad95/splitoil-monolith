package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Car extends AbstractValue {
    @NonNull private CarId carId;
    @NonNull private UUID driverId;
    private int numberOfSeats;

}
