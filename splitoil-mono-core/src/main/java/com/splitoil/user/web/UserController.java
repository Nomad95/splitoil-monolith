package com.splitoil.user.web;

import com.splitoil.shared.dto.Result;
import com.splitoil.user.application.UserFacade;
import com.splitoil.user.web.dto.RegisterUserCommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody @Valid @NonNull final RegisterUserCommand registerUserCommand) {
        final Result result = userFacade.registerUser(registerUserCommand);
        if (result == Result.Success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
