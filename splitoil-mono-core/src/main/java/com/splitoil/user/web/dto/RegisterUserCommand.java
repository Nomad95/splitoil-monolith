package com.splitoil.user.web.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterUserCommand {
    @NonNull
    private String login;

    @NonNull
    private String password;

    @Email
    @NonNull
    private String email;

    @NonNull
    private String defaultCurrency;

}
