package com.splitoil.travel.travel.domain.model

import com.splitoil.UnitTest
import com.splitoil.infrastructure.json.JacksonAdapter
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class TravelParticipantsSerializationTest extends Specification {

    public static final String EMPTY = '{"participants":[]}'
    public static final String PARTICIPANTS = '{"participants":[{"participantId":"1435ba24-fd5d-4d9a-b19f-6224f0b2e210","assignedCarId":"bb1981e6-047c-417b-8e44-10ac303ce4cb"},{"participantId":"c37e58b4-2421-44c1-a46e-5108c48f4438","assignedCarId":"bb1981e6-047c-417b-8e44-10ac303ce4cb"},{"participantId":"1b6a488f-237a-4bad-9f84-c2200aa6f9cf","assignedCarId":"879ac18f-a6e1-4aa1-a237-dc7acb135aa2"}]}'
    private static final UUID PARTICIPANT_1 = UUID.fromString('1435ba24-fd5d-4d9a-b19f-6224f0b2e210')
    private static final UUID PARTICIPANT_2 = UUID.fromString('c37e58b4-2421-44c1-a46e-5108c48f4438')
    private static final UUID PARTICIPANT_3 = UUID.fromString('1b6a488f-237a-4bad-9f84-c2200aa6f9cf')
    private static final UUID CAR_1 = UUID.fromString('bb1981e6-047c-417b-8e44-10ac303ce4cb')
    private static final UUID CAR_2 = UUID.fromString('879ac18f-a6e1-4aa1-a237-dc7acb135aa2')


    def 'serialize empty'() {
        when:
            def participants = new TravelParticipants()
            def json = JacksonAdapter.getInstance().toJson(participants)

        then:
            json == EMPTY
    }

    def 'deserialize empty'() {
        when:
            def participants = new TravelParticipants()
            def fromJson = JacksonAdapter.getInstance().jsonDecode(EMPTY, TravelParticipants)

        then:
            participants == fromJson
    }

    def 'serialize participants'() {
        when:
            def participant1 = new TravelParticipant(PARTICIPANT_1, CAR_1)
            def participant2 = new TravelParticipant(PARTICIPANT_2, CAR_1)
            def participant3 = new TravelParticipant(PARTICIPANT_3, CAR_2)
            def participants = new TravelParticipants([participant1, participant2, participant3])

            def json = JacksonAdapter.getInstance().toJson(participants)

        then:
            json == PARTICIPANTS
    }

    def 'deserialize participants'() {
        when:
            def participant1 = new TravelParticipant(PARTICIPANT_1, CAR_1)
            def participant2 = new TravelParticipant(PARTICIPANT_2, CAR_1)
            def participant3 = new TravelParticipant(PARTICIPANT_3, CAR_2)
            def participants = new TravelParticipants([participant1, participant2, participant3])
            def fromJson = JacksonAdapter.getInstance().jsonDecode(PARTICIPANTS, TravelParticipants)

        then:
            participants == fromJson
    }


}
