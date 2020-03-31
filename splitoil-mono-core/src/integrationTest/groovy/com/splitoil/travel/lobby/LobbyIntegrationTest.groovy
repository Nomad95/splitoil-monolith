package com.splitoil.travel.lobby

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.infrastructure.security.WithMockSecurityContext
import com.splitoil.travel.lobby.web.dto.AddCarToTravelCommand
import com.splitoil.travel.lobby.web.dto.ChangeTravelDefaultCurrencyCommand
import com.splitoil.travel.lobby.web.dto.CreateLobbyCommand
import com.splitoil.travel.lobby.web.dto.SetTravelTopRatePer1kmCommand
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
    private static final UUID CAR_UUID = UUID.fromString('c13d02de-c7a4-4ab0-9f3f-7ee0768b4a5f')
    private static final BigDecimal TOP_RATE = new BigDecimal("3.50")
    private static final String USD = 'USD'


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

    @Sql(scripts = '/db/travel/lobby/new_lobby.sql')
    def "Driver chooses travel car to lobby"() {
        given:
            def command = AddCarToTravelCommand.of(LOBBY_UUID, CAR_UUID)

        when:
            def result = mockMvc.perform(post("/lobby/car")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.content').value("Success"))
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

}
