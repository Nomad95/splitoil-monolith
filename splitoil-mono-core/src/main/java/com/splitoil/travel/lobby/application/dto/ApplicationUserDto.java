package com.splitoil.travel.lobby.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUserDto {
    private UUID id;

    private String login;

    private String email;

    private String defaultCurrency;

}
