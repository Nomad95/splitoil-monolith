package com.splitoil.travel.travelflow.domain.model

import com.splitoil.UnitTest
import com.splitoil.infrastructure.json.JacksonAdapter
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class InitialStateSerializationTest extends Specification {

    public static final String EMPTY = '{"carStates":{}}'
    public static final String STATE = '{"carStates":{"bb1981e6-047c-417b-8e44-10ac303ce4cb":{"fuelLevel":1.23,"odometer":1234}}}'
    private static final UUID CAR_1 = UUID.fromString('bb1981e6-047c-417b-8e44-10ac303ce4cb')
    private static final CarState CAR_STATE = new CarState(new BigDecimal("1.23"), 1234)


    def 'serialize empty'() {
        when:
            def state = new InitialState()
            def json = JacksonAdapter.getInstance().toJson(state)

        then:
            json == EMPTY
    }

    def 'deserialize empty'() {
        when:
            def state = new InitialState()
            def fromJson = JacksonAdapter.getInstance().jsonDecode(EMPTY, InitialState)

        then:
            state == fromJson
    }

    def 'serialize state'() {
        when:
            def state = new InitialState()
            state.addCarState(CAR_1, CAR_STATE)

            def json = JacksonAdapter.getInstance().toJson(state)

        then:
            json == STATE
    }

    def 'deserialize state'() {
        when:
            def state = new InitialState()
            state.addCarState(CAR_1, CAR_STATE)
            def fromJson = JacksonAdapter.getInstance().jsonDecode(STATE, InitialState)

        then:
            state == fromJson
    }


}
