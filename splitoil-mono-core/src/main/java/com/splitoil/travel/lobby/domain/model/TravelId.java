package com.splitoil.travel.lobby.domain.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelId {
    private @NonNull UUID travelId;
}
