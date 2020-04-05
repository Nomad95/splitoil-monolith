package com.splitoil.travel.lobby.infrastructure;

import com.splitoil.shared.UserCurrencyProvider;
import com.splitoil.shared.event.EventPublisher;
import com.splitoil.shared.event.publisher.NoopEventPublisher;
import com.splitoil.shared.model.Currency;
import com.splitoil.travel.lobby.application.CarTranslationService;
import com.splitoil.travel.lobby.application.LobbyService;
import com.splitoil.travel.lobby.application.UserTranslationService;
import com.splitoil.travel.lobby.domain.model.LobbyCreator;
import com.splitoil.travel.lobby.domain.model.LobbyRepository;
import com.splitoil.travel.lobby.infrastructure.database.InMemoryLobbyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LobbyConfiguration {

    public LobbyService lobbyService(final UserTranslationService userTranslationService, final CarTranslationService carTranslationService) {
        return new LobbyService(new LobbyCreator(StubCurrencyProvider.getInstance()), new InMemoryLobbyRepository(), new NoopEventPublisher(),
            userTranslationService, carTranslationService);
    }

    @Bean
    public LobbyService lobbyService(final LobbyRepository lobbyRepository, final EventPublisher eventPublisher,
        final UserCurrencyProvider userCurrencyProvider, final UserTranslationService userTranslationService, final CarTranslationService carTranslationService) {
        final LobbyCreator lobbyCreator = new LobbyCreator(userCurrencyProvider);
        return new LobbyService(lobbyCreator, lobbyRepository, eventPublisher, userTranslationService, carTranslationService);
    }

    @Bean
    public LobbyCreator lobbyCreator(final UserCurrencyProvider userCurrencyProvider) {
        return new LobbyCreator(userCurrencyProvider);
    }

    public static class StubCurrencyProvider implements UserCurrencyProvider {
        private static StubCurrencyProvider INSTANCE;

        private Currency currency = Currency.PLN;

        public static StubCurrencyProvider getInstance() {
            if (INSTANCE == null)
                INSTANCE = new StubCurrencyProvider();
            return INSTANCE;
        }

        @Override
        public Currency getCurrentUserDefaultCurrency() {
            return currency;
        }

        void set(final Currency currency) {
            this.currency = currency;
        }

        public static void setDefaultCurrency(final Currency currency) {
            final StubCurrencyProvider instance = getInstance();
            instance.set(currency);
        }
    }

}
