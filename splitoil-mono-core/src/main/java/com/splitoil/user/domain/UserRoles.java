package com.splitoil.user.domain;

import com.splitoil.infrastructure.json.JsonEntity;
import com.splitoil.shared.AbstractValue;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRoles extends AbstractValue implements JsonEntity, Serializable {

    @NotNull
    @NotEmpty
    private Set<String> roles;

    public Set<String> getRoles() {
        return new HashSet<>(roles);
    }

    UserRoles(final Set<String> roles) {
        this.roles = new HashSet<>(roles);
    }
}
