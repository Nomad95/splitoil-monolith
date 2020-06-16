package com.splitoil.travel.travel.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StateOutputDto {
    private List<CarStateOutputDto> carsState;
}
