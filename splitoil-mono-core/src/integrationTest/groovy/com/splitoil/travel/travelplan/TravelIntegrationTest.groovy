package com.splitoil.travel.travelplan

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.infrastructure.security.WithMockSecurityContext
import com.splitoil.travel.travelflow.web.dto.*
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
@WithMockUser("admin")
@WithMockSecurityContext
class TravelIntegrationTest extends IntegrationSpec {

    private static final UUID CAR_UUID = UUID.fromString('b9574b12-8ca1-4779-aab8-a25192e33739')
    private static final UUID SECOND_CAR_UUID = UUID.fromString('2e1d5068-61e6-445a-a65e-9b8117b707d6')

    private static final UUID PASSENGER_ID = UUID.fromString('31c7b8b8-17fe-46b2-9ec6-df4c1d23f3a4')
    private static final UUID TRAVEL_ID = UUID.fromString('799c470e-b0d6-4df9-8bbe-195533515826')
    private static final UUID DESTINATION_WAYPOINT_ID = UUID.fromString('1b6a488f-237a-4bad-9f84-c2200aa6f9cf')
    private static final UUID STOP_WAYPOINT_ID = UUID.fromString('e4a6bf07-b429-464d-8e1c-554aac5e543e')
    private static final UUID CHECKPOINT_WAYPOINT_ID = UUID.fromString('4db9b61e-6ae8-472f-93ee-6a90e5cb6dae')
    private static final GeoPointDto GEO_POINT = GeoPointDto.of(10, 15)


    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
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
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can add refuel place"() {
        given:
            def reseatPlaceCommand = AddRefuelPlaceCommand.of(TRAVEL_ID, GEO_POINT, CAR_UUID)

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

        when:
            def result = mockMvc.perform(post("/travel/route/boarding")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(stopPlaceCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('PARTICIPANT_BOARDING_PLACE'))
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

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_begin_and_end.sql'])
    def "Lobby creator can change location of a waypoint"() {
        given:
            def moveWaypointCommand = MoveWaypointCommand.of(TRAVEL_ID, DESTINATION_WAYPOINT_ID, GEO_POINT)

        when:
            def result = mockMvc.perform(post("/travel/route/changelocation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(moveWaypointCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('DESTINATION_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].location.lon').value(10))
                    .andExpect(jsonPath('$.waypoints[1].location.lat').value(15))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_some_waypoints.sql'])
    def "Lobby creator can change order of waypoints"() {
        given:
            def changeOrderWaypointCommand = ChangeOrderWaypointCommand.of(TRAVEL_ID, STOP_WAYPOINT_ID, CHECKPOINT_WAYPOINT_ID)

        when:
            def result = mockMvc.perform(post("/travel/route/changeorder")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(changeOrderWaypointCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('CHECKPOINT'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('STOP_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_some_waypoints.sql'])
    def "Lobby creator can delete waypoint"() {
        given:
            def changeOrderWaypointCommand = DeleteWaypointCommand.of(TRAVEL_ID, STOP_WAYPOINT_ID)

        when:
            def result = mockMvc.perform(delete("/travel/route/waypoint")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(changeOrderWaypointCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.waypoints[0].waypointType').value('BEGINNING_PLACE'))
                    .andExpect(jsonPath('$.waypoints[1].waypointType').value('CHECKPOINT'))
                    .andExpect(jsonPath('$.waypoints[2].waypointType').value('DESTINATION_PLACE'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/travel/travel/new_travel_two_drivers_three_pass_with_some_waypoints.sql'])
    def "Lobby creator can confirm travel"() {
        given:
            def confirmTravelPlanCommand = ConfirmTravelPlanCommand.of(TRAVEL_ID)

        when:
            def result = mockMvc.perform(post("/travel/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(confirmTravelPlanCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.travelStatus').value('IN_CONFIRMATION'))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/travel/travel/confirmed_travel_with_some_waypoints.sql'])
    def "Lobby creator can add initial car state to have its travel cost calculated correctly"() {
        given:
            def carInitialStateCommand = SetCarInitialStateCommand.of(TRAVEL_ID, CAR_UUID, BigDecimal.TEN, 1234)

        when:
            def result = mockMvc.perform(post("/travel/carstate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(carInitialStateCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.travelStatus').value('IN_CONFIRMATION'))
                    .andExpect(jsonPath('$.state.carsState[0].carId').value(CAR_UUID.toString()))
                    .andExpect(jsonPath('$.state.carsState[0].fuelAmount').value(new BigDecimal("10.0")))
                    .andExpect(jsonPath('$.state.carsState[0].odometer').value(1234))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql',
            '/db/travel/travel/confirmed_travel_with_initial_state.sql'])
    def "Lobby creator can start travel when all is set up correctly"() {
        given:
            def startTravel = StartTravelCommand.of(TRAVEL_ID)

        when:
            def result = mockMvc.perform(post("/travel/start")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(startTravel)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.travelStatus').value('IN_TRAVEL'))
    }
}
