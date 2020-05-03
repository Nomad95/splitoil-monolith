package com.splitoil.travel.lobby

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.infrastructure.security.WithMockSecurityContext
import com.splitoil.travel.travelflow.web.dto.*
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
@WithMockUser("admin")
@WithMockSecurityContext
class TravelIntegrationTest extends IntegrationSpec {

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
    private static final UUID TRAVEL_ID = UUID.fromString('799c470e-b0d6-4df9-8bbe-195533515826')
    private static final GeoPointDto GEO_POINT = GeoPointDto.of(10, 15)


    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass.sql'])
    def "Lobby creator can add travel beginning"() {
        given:
            def selectBeginningCommand = SelectTravelBeginningCommand.of(TRAVEL_ID, GEO_POINT)

        when:
            def result = mockMvc.perform(post("/travel/route/beginning")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(selectBeginningCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_beginning.sql'])
    def "Lobby creator can add travel destination"() {
        given:
            def travelDestinationCommand = SelectTravelDestinationCommand.of(TRAVEL_ID, GEO_POINT)

        when:
            def result = mockMvc.perform(post("/travel/route/destination")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(travelDestinationCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add reseat place"() {
        given:
            def reseatPlaceCommand = AddReseatPlaceCommand.of(TRAVEL_ID, GEO_POINT, CAR_UUID, SECOND_CAR_UUID, PASSENGER_ID)

        when:
            def result = mockMvc.perform(post("/travel/route/reseat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(reseatPlaceCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('RESEAT_PLACE'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add refuel place"() {
        given:
            def reseatPlaceCommand = AddRefuelPlaceCommand.of(TRAVEL_ID, GEO_POINT, new BigDecimal("123"), new BigDecimal("123"))//TODO: refuel a specifuic car :)

        when:
            def result = mockMvc.perform(post("/travel/route/refuel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(reseatPlaceCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('REFUEL_PLACE'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add stop place"() {
        given:
            def stopPlaceCommand = AddStopPlaceCommand.of(TRAVEL_ID, GEO_POINT)

        when:
            def result = mockMvc.perform(post("/travel/route/stop")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(stopPlaceCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('STOP_PLACE'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add passenger boarding place"() {
        given:
            def stopPlaceCommand = AddParticipantBoardingPlaceCommand.of(TRAVEL_ID, GEO_POINT, PASSENGER_ID, CAR_UUID)
            //TODO: sprawdzenie czy moze dolaczyc do samochodu w lobby, w travel powinno sie uzyc tylko istniejacych passengerow w lobby
            //TODO: sprawdz czy passenger moze tak i w reszcie przypadków ;)

        when:
            def result = mockMvc.perform(post("/travel/route/boarding")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(stopPlaceCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('PARTICIPANT_BOARDING_PLACE'))//TODO: czy  car driver moze tak o se uciec???
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add passenger exit place"() {
        given:
            def exitPlaceCommand = AddParticipantExitPlaceCommand.of(TRAVEL_ID, GEO_POINT, PASSENGER_ID, CAR_UUID)

        when:
            def result = mockMvc.perform(post("/travel/route/exit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(exitPlaceCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('PARTICIPANT_EXIT_PLACE'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/user/user_passenger.sql',
            '/db/user/user_passenger_2.sql',
            '/db/user/user_passenger_3.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql',
            '/db/travel/lobby/travel_participant_passenger.sql',
            '/db/travel/lobby/travel_participant_passenger_2.sql',
            '/db/travel/lobby/travel_participant_passenger_3.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add checkpoint"() {
        given:
            def checkpointCommand = AddCheckpointCommand.of(TRAVEL_ID, GEO_POINT)

        when:
            def result = mockMvc.perform(post("/travel/route/checkpoint")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(checkpointCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('CHECKPOINT'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }
}
