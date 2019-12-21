package com.splitoil.gasstation

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.gasstation.dto.AddGasStationToObservableInputDto
import com.splitoil.gasstation.dto.GeoPointInputDto
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
class GasStationIntegrationTest extends IntegrationSpec {

    def "driver adds a gas station to observables"() {
        given:
            def command = new AddGasStationToObservableInputDto(new GeoPointInputDto(25, 100), "Name", 1L)

        when:
            def result = mockMvc.perform(post("/gas-station/observe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andExpect(status().isOk())
    }

    def "driver gets his own observed gas station list"() {
        given:
            def command = new AddGasStationToObservableInputDto(new GeoPointInputDto(25, 100), "Name", 1L)

        when:
            def result = mockMvc.perform(post("/gas-station/observe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andExpect(status().isOk())

        when:
            def observables = mockMvc.perform(get("/gas-station/observe/driver/1")
                    .contentType(MediaType.APPLICATION_JSON))

        then:
            observables
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath('$[0].name').value("Name"))
                    .andExpect(jsonPath('$[0].location.lon').value(25))
                    .andExpect(jsonPath('$[0].location.lat').value(100))
    }

}
