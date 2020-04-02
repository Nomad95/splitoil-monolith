package com.splitoil.user.domain;

import com.splitoil.infrastructure.json.JsonUserType;
import com.splitoil.shared.AbstractEntity;
import com.splitoil.shared.model.Currency;
import com.splitoil.user.dto.ApplicationUserDto;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUser extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Currency defaultCurrency;

    @Column(nullable = false, columnDefinition = "json")
    @Type(type = "com.splitoil.infrastructure.json.JsonUserType",
          parameters = {
              @org.hibernate.annotations.Parameter(name = JsonUserType.OBJECT, value = "com.splitoil.user.domain.UserRoles"),
          })
    private UserRoles roles;

    public Set<GrantedAuthority> getAuthorities() {
        return roles.getRoles().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableSet());
    }

    public ApplicationUserDto toDetailsDto() {
        return ApplicationUserDto.builder()
            .id(getAggregateId())
            .defaultCurrency(defaultCurrency.name())
            .email(email)
            .login(login)
            .build();
    }

//    public static ApplicationUser create(final UserInputDTO userInputDTO) {
//        return new ApplicationUser(
//            userInputDTO.getUsername(),
//            userInputDTO.getPassword(),
//            new UserRole(ImmutableSet.of(Role.USER.name())));
//    }
}
