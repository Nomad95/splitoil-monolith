package com.splitoil.car.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DriverDto {

    private Long id;
}
