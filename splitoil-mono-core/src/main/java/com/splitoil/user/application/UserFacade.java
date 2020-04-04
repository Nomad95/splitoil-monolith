package com.splitoil.user.application;

import com.splitoil.shared.annotation.ApplicationService;
import com.splitoil.shared.dto.Result;
import com.splitoil.user.domain.ApplicationUser;
import com.splitoil.user.domain.UserCreator;
import com.splitoil.user.domain.UserRepository;
import com.splitoil.user.dto.ApplicationUserDto;
import com.splitoil.user.web.dto.RegisterUserCommand;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@ApplicationService
@AllArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    private final UserCreator userCreator;

    public Result registerUser(final RegisterUserCommand registerUserCommand) {
        final ApplicationUser applicationUser = userCreator.createApplicationUser(registerUserCommand);
        userRepository.save(applicationUser);

        return Result.Success;
    }

    public ApplicationUserDto getUserByLogin(final String login) {
        return userRepository.getOneByLogin(login).toDetailsDto();
    }

    public ApplicationUserDto getUserById(final UUID userId) {
        return userRepository.getOneById(userId).toDetailsDto();
    }
}
