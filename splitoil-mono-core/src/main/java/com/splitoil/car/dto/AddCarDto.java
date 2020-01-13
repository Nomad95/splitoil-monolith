package com.splitoil.car.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddCarDto {

    @NotBlank
    private String brand;

    @NotBlank
    private String name;

    @NonNull
    private DriverDto driver;
}
