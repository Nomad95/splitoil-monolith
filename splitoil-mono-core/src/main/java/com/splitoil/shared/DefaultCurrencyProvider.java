package com.splitoil.shared;

import com.splitoil.shared.model.Currency;
import com.splitoil.user.domain.ApplicationUser;
import com.splitoil.user.domain.UserRepository;
import com.splitoil.user.shared.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

//TODO: zastanowic sie gdzie ta klasa powinna byc
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class DefaultCurrencyProvider implements UserCurrencyProvider {

    private final UserRepository userRepository;
    //TODO: maybe change to appService?
    //TODO: taki bean z odświeżaniem :)

    private Currency currentUserDefaultCurrency;

    @Autowired
    public DefaultCurrencyProvider(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final ApplicationUser oneByLogin = userRepository.getOneByLogin(currentUserLogin);
        this.currentUserDefaultCurrency = oneByLogin.getDefaultCurrency();
    }

    @Override
    public Currency getCurrentUserDefaultCurrency() {
        if (Objects.isNull(currentUserDefaultCurrency)) {
            throw new NullPointerException("No default currency set");
        }

        return currentUserDefaultCurrency;
    }
}
