package com.splitoil.travel.lobby

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.infrastructure.security.WithMockSecurityContext
import com.splitoil.travel.lobby.web.dto.*
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
@WithMockUser("admin")
@WithMockSecurityContext
class LobbyIntegrationTest extends IntegrationSpec {

    private static final String LOBBY_NAME = "Some lobby name"
    private static final UUID LOBBY_UUID = UUID.fromString('148091b1-c0f0-4a3e-9b0d-569e05cfcd0f') //db script

    private static final UUID CAR_UUID = UUID.fromString('b9574b12-8ca1-4779-aab8-a25192e33739')
    private static final UUID SECOND_CAR_UUID = UUID.fromString('2e1d5068-61e6-445a-a65e-9b8117b707d6')

    private static final BigDecimal TOP_RATE = new BigDecimal("3.50")
    private static final String USD = 'USD'

    private static final String DRIVER_ID_STR = '0ea7db01-5f68-409b-8130-e96e8d96060a'
    private static final UUID DRIVER_ID = UUID.fromString('0ea7db01-5f68-409b-8130-e96e8d96060a')

    private static final UUID PASSENGER_ID = UUID.fromString('31c7b8b8-17fe-46b2-9ec6-df4c1d23f3a4')

    def "Driver creates lobby"() {
        given:
            def command = CreateLobbyCommand.of(LOBBY_NAME)

        when:
            def result = mockMvc.perform(post("/lobby")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath('$.lobbyId').exists())
                    .andExpect(jsonPath('$.lobbyStatus').value("IN_CREATION"))
    }

    @Sql(scripts = ["/db/travel/lobby/new_lobby.sql", "/db/car/default_car.sql", "/db/user/user_admin.sql", "/db/user/user_driver_1.sql"])
    def "Driver chooses travel car to lobby"() {
        given:
            def command = AddCarToTravelCommand.of(LOBBY_UUID, CAR_UUID, DRIVER_ID)

        when:
            def result = mockMvc.perform(post("/lobby/car")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then: 'Car is added as well as Driver, as a participant himself'
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.participants[0].userId').value(DRIVER_ID_STR))
                    .andExpect(jsonPath('$.participants[0].assignedCar').value(CAR_UUID.toString()))
    }

    @Sql(scripts = '/db/travel/lobby/new_lobby.sql')
    def "Should get lobby details"() {
        when:
            def result = mockMvc.perform(get("/lobby/" + LOBBY_UUID)
                    .contentType(MediaType.APPLICATION_JSON))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.lobbyStatus').value("IN_CREATION"))
    }

    @Sql(scripts = '/db/travel/lobby/new_lobby_in_configuration.sql')
    def "Should configure top rate in lobby"() {
        given:
            def setTravelTopRatePer1kmCommand = SetTravelTopRatePer1kmCommand.of(LOBBY_UUID, TOP_RATE)

        when:
            def result = mockMvc.perform(post("/lobby/topRate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(setTravelTopRatePer1kmCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.lobbyStatus').value("IN_CONFIGURATION"))
                    .andExpect(jsonPath('$.topRatePer1km').value("3.5"))
    }

    @Sql(scripts = '/db/travel/lobby/new_lobby_in_configuration.sql')
    def "Should configure default currency for travel in lobby"() {
        given:
            def changeTravelDefaultCurrencyCommand = ChangeTravelDefaultCurrencyCommand.of(LOBBY_UUID, USD)

        when:
            def result = mockMvc.perform(post("/lobby/currency")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(changeTravelDefaultCurrencyCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.lobbyStatus').value("IN_CONFIGURATION"))
                    .andExpect(jsonPath('$.travelCurrency').value("USD"))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_in_configuration.sql', '/db/user/user_passenger.sql', '/db/travel/lobby/travel_participant_lobby_creator_driver.sql'])
    def "Driver can add passenger to lobby"() {
        given:
            def addPassengerToLobbyCommand = AddPassengerToLobbyCommand.of(LOBBY_UUID, PASSENGER_ID, CAR_UUID)

        when:
            def result = mockMvc.perform(post("/lobby/participant/passenger")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addPassengerToLobbyCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.participants[0].userId').value(DRIVER_ID.toString()))
                    .andExpect(jsonPath('$.participants[0].participantType').value("CAR_DRIVER"))
                    .andExpect(jsonPath('$.participants[1].userId').value(PASSENGER_ID.toString()))
                    .andExpect(jsonPath('$.participants[1].participantType').value("PASSENGER"))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_in_configuration.sql', '/db/user/user_passenger.sql', '/db/travel/lobby/travel_participant_lobby_creator_driver.sql'])
    def "Driver can add temporal passenger to lobby"() {
        given:
            def addPassengerToLobbyCommand = AddTemporalPassengerToLobbyCommand.of(LOBBY_UUID, 'passenger', CAR_UUID)

        when:
            def result = mockMvc.perform(post("/lobby/participant/temporalpassenger")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addPassengerToLobbyCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.participants[0].userId').value(DRIVER_ID.toString()))
                    .andExpect(jsonPath('$.participants[0].participantType').value("CAR_DRIVER"))
                    .andExpect(jsonPath('$.participants[1].displayName').value('passenger'))
                    .andExpect(jsonPath('$.participants[1].participantType').value("TEMPORAL_PASSENGER"))
    }

    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql', '/db/user/user_passenger.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql', '/db/travel/lobby/travel_participant_passenger.sql'])
    def "Driver can reseat temporal passengers"() {
        given:
            def assignToCarCommand = AssignToCarCommand.of(LOBBY_UUID, PASSENGER_ID, SECOND_CAR_UUID)

        when:
            def result = mockMvc.perform(post("/lobby/participant/assignToCar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(assignToCarCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())

                    .andExpect(jsonPath('$.lobbyId').value(LOBBY_UUID.toString()))
                    .andExpect(jsonPath('$.participants[0].userId').value(DRIVER_ID.toString()))
                    .andExpect(jsonPath('$.participants[0].participantType').value("CAR_DRIVER"))
                    .andExpect(jsonPath('$.participants[0].assignedCar').value(CAR_UUID.toString()))

                    .andExpect(jsonPath('$.participants[1].displayName').value('Wojtapszenks'))
                    .andExpect(jsonPath('$.participants[1].participantType').value("PASSENGER"))
                    .andExpect(jsonPath('$.participants[1].assignedCar').value(SECOND_CAR_UUID.toString()))
    }


    @Sql(scripts = ['/db/travel/lobby/new_lobby_with_passenger.sql', '/db/user/user_passenger.sql',
            '/db/travel/lobby/travel_participant_lobby_creator_driver.sql', '/db/travel/lobby/travel_participant_passenger.sql'])
    def "Driver can start defining travelling plan"() {
        given:
            def startDefiningTravelPlanCommand = StartDefiningTravelPlanCommand.of(LOBBY_UUID)

        when:
            def result = mockMvc.perform(post("/lobby/starttravelplan")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(startDefiningTravelPlanCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isAccepted())
    }

}
