package com.splitoil.gasstation

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.gasstation.dto.*
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
class GasStationIntegrationTest extends IntegrationSpec {

    public static final int LONGITUDE = 25
    public static final int LATITUDE = 100
    public static final String NAME = "Name"
    public static final long DRIVER_ID = 1L

    def "driver adds a gas station to observables"() {
        given:
            def gasStationIdDto = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), NAME)
            def driverDto = DriverDto.of(DRIVER_ID)
            def command = new AddToObservableDto(gasStationIdDto, driverDto)

        when:
            def result = mockMvc.perform(post("/gas-station/observe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andExpect(status().isOk())
    }

    def "driver gets his own observed gas station list"() {
        given:
            def gasStationIdDto = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), NAME)
            def driverDto = DriverDto.of(DRIVER_ID)
            def command = new AddToObservableDto(gasStationIdDto, driverDto)

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
                    .andExpect(jsonPath('$[0].name').value(NAME))
                    .andExpect(jsonPath('$[0].location.lon').value(LONGITUDE))
                    .andExpect(jsonPath('$[0].location.lat').value(LATITUDE))
    }

    def "driver can rate gas station"() {
        given:
            def gasStationIdDto = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), NAME)
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 4)

        when:
            def result = mockMvc.perform(post("/gas-station/rate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addRatingCommand)))

        then:
            result.andExpect(status().isOk())
    }

    def "driver can see gas station rating"() {
        given:
            def gasStationIdDto = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), NAME)
            print(jackson.toJson(gasStationIdDto))
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 4)

        when:
            def result = mockMvc.perform(post("/gas-station/rate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addRatingCommand)))

        then:
            result.andExpect(status().isOk())

        when:
            def ratings = mockMvc.perform(post("/gas-station/rating")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(gasStationIdDto)))

        then:
            ratings
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$').value(4.0))
    }

}
