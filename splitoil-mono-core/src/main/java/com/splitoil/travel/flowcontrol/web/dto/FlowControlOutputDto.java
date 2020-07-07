package com.splitoil.travel.flowcontrol.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlowControlOutputDto {
    private UUID id;
    private UUID carId;
    private UUID travelId;

}
