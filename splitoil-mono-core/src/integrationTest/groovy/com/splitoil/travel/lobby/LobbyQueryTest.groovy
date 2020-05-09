package com.splitoil.travel.lobby

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.infrastructure.security.WithMockSecurityContext
import com.splitoil.travel.lobby.application.LobbyQuery
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql

@Category(IntegrationTest)
@WithMockUser("admin")
@WithMockSecurityContext
class LobbyQueryTest extends IntegrationSpec {

    private static final String LOBBY_NAME = "Some lobby name"
    private static final UUID LOBBY_UUID = UUID.fromString('148091b1-c0f0-4a3e-9b0d-569e05cfcd0f') //db script

    private static final UUID CAR_UUID = UUID.fromString('b9574b12-8ca1-4779-aab8-a25192e33739')
    private static final UUID SECOND_CAR_UUID = UUID.fromString('2e1d5068-61e6-445a-a65e-9b8117b707d6')

    private static final BigDecimal TOP_RATE = new BigDecimal("3.50")
    private static final String USD = 'USD'

    private static final String DRIVER_ID_STR = '0ea7db01-5f68-409b-8130-e96e8d96060a'
    private static final UUID DRIVER_ID = UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')
    private static final UUID SECOND_DRIVER_ID = UUID.fromString('c13d02de-c7a4-4ab0-9f3f-7ee0768b4a5f')

    private static final UUID PASSENGER_ID = UUID.fromString('31c7b8b8-17fe-46b2-9ec6-df4c1d23f3a4')

    @Autowired
    LobbyQuery lobbyQuery

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql'])
    def "Cars should exist in lobby"() {
        when:
            def exists = lobbyQuery.carsExistInLobby(List.of(CAR_UUID, SECOND_CAR_UUID), LOBBY_UUID)

        then:
            exists
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql'])
    def "Car should exist in lobby"() {
        when:
            def exists = lobbyQuery.carExistInLobby(SECOND_CAR_UUID, LOBBY_UUID)

        then:
            exists
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql', '/db/travel/lobby/travel_participant_passenger.sql'])
    def "Passenger should exist in lobby"() {
        when:
            def exists = lobbyQuery.participantExistsInLobby(PASSENGER_ID, LOBBY_UUID)

        then:
            exists
    }

}
