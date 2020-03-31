package com.splitoil.user.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUserDto {
    private Long id;

    private String login;

    private String email;

    private String defaultCurrency;

}
