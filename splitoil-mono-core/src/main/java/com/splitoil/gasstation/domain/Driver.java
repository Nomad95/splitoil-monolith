package com.splitoil.gasstation.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Driver {

    private Long driverId;
}
