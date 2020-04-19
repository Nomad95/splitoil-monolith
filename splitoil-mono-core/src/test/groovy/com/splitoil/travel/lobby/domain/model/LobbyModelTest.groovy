package com.splitoil.travel.lobby.domain.model


import spock.lang.Specification

class LobbyModelTest extends Specification {
    protected static final UUID DRIVER_ID = UUID.fromString('f24a60d3-d8f3-4a6d-8f2f-cacd68586b59')
    protected static final UUID SECOND_DRIVER_ID = UUID.fromString('25151ea3-239d-453f-bc49-d0345d9210bf')
    protected static final UUID CAR_ID = UUID.fromString('1ad11d82-5f49-4761-abc3-f8cd9b9bd594')
    protected static final UUID SECOND_CAR_ID = UUID.fromString('29c9c416-cad6-4845-97a0-3d2b1bf496e9')
    protected static final UUID PASSENGER_1_ID = UUID.fromString('1a26cbe6-1c70-42a2-8734-6b861702672d')

    protected static final String LOBBY_NAME = "Some lobby name"
    protected static final String PASSENGER_NAME = 'Wojtaszko'
    protected static final String DRIVER_LOGIN = 'Wojtapsiux'
    protected static final String SECOND_DRIVER_LOGIN = 'Wantajaks'

    protected static final String PLN = 'PLN'
    protected static final String EUR = 'EUR'
    protected static final String USD = 'USD'

}
