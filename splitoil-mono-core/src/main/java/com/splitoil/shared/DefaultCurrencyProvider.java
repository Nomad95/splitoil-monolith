package com.splitoil.shared;

import com.splitoil.shared.model.Currency;
import com.splitoil.user.application.UserFacade;
import com.splitoil.user.dto.ApplicationUserDto;
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

    private final UserFacade userFacade;
    //TODO: taki bean z odświeżaniem :)

    private Currency currentUserDefaultCurrency;

    @Autowired
    public DefaultCurrencyProvider(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostConstruct
    public void init() {
        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final ApplicationUserDto oneByLogin = userFacade.getUserByLogin(currentUserLogin);
        this.currentUserDefaultCurrency = Currency.valueOf(oneByLogin.getDefaultCurrency());
    }

    @Override
    public Currency getCurrentUserDefaultCurrency() {
        if (Objects.isNull(currentUserDefaultCurrency)) {
            throw new NullPointerException("No default currency set");
        }

        return currentUserDefaultCurrency;
    }
}
