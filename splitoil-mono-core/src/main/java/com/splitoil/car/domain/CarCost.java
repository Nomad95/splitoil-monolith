package com.splitoil.car.domain;

import com.splitoil.shared.AbstractEntity;
import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CarCost extends AbstractEntity {

    private String name;

    private Instant costDate;

    private BigDecimal value;

    private Long carId;

}
