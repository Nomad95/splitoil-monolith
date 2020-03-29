package com.splitoil.infrastructure;

import com.splitoil.shared.UserCurrencyProvider;
import com.splitoil.shared.model.Currency;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Primary
@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TestCurrencyProvider implements UserCurrencyProvider {

    private Currency currency = Currency.PLN;

    @Override
    public Currency getCurrentUserDefaultCurrency() {
        return currency;
    }

    public void setDefaultCurrency(final Currency currency) {
        this.currency = currency;
    }
}
