package com.splitoil.travel.lobby.domain.model;

import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.persistence.Embeddable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarId extends AbstractValue {
    @NonNull private UUID carId;
}
