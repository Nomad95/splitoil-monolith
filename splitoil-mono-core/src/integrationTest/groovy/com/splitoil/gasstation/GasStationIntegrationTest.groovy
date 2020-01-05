package com.splitoil.gasstation

import com.splitoil.IntegrationTest
import com.splitoil.TestUtils
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
    public static final BigDecimal DEFAULT_PRICE = new BigDecimal("4.59")
    public static final String BENZINE_95_STR = "BENZINE_95"
    public static final String CURRENCY_PLN_STR = "PLN"
    public static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), NAME)

    def "driver adds a gas station to observables"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def driverDto = DriverDto.of(DRIVER_ID)
            def command = new AddToObservableDto(gasStationIdDto, driverDto)

        when:
            def result = mockMvc.perform(post("/gas-station/observe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(command)))

        then:
            result.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
    }

    def "driver gets his own observed gas station list"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
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
                    .andExpect(jsonPath('$._embedded.gasStationIdDtoList.[0].name').value(NAME))
                    .andExpect(jsonPath('$._embedded.gasStationIdDtoList.[0].location.lon').value(LONGITUDE))
                    .andExpect(jsonPath('$._embedded.gasStationIdDtoList.[0].location.lat').value(LATITUDE))
    }

    def "driver can rate gas station"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 4)

        when:
            def result = mockMvc.perform(post("/gas-station/rate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addRatingCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
    }

    def "driver can see gas station rating"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
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
                    .andExpect(jsonPath('$.content').value(4.0))
    }

    def "driver adds petrol price to gas station"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addPetrolPriceCommand = new AddPetrolPriceDto(gasStationIdDto, DEFAULT_PRICE, CURRENCY_PLN_STR, BENZINE_95_STR)

        when:
            def result = mockMvc.perform(post("/gas-station/gas-price")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addPetrolPriceCommand)))

        then:
            result.andExpect(status().isAccepted())
    }


    //TODO: i think this should be done internally
    def "system should accept gas price"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addPetrolPriceCommand = new AddPetrolPriceDto(gasStationIdDto, DEFAULT_PRICE, CURRENCY_PLN_STR, BENZINE_95_STR)

        when:
            def request = mockMvc.perform(post("/gas-station/gas-price")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addPetrolPriceCommand)))

        then:
            def resultJson = request.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString()
            String resultUuid = TestUtils.extractContent(resultJson)

        when:
            def uuid = UUID.fromString(resultUuid)
            def acceptCommand = AcceptPetrolPriceDto.builder().gasStationIdDto(GAS_STATION_ID_DTO).priceUuid(uuid).build()
            def nextRequest = mockMvc.perform(post("/gas-station/gas-price/accept")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(acceptCommand)))

        then:
            nextRequest.andExpect(status().isOk())
    }

    def "should get gas price from gas station by type and currency"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addPetrolPriceCommand = new AddPetrolPriceDto(gasStationIdDto, DEFAULT_PRICE, CURRENCY_PLN_STR, BENZINE_95_STR)
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType("BENZINE_95")
                    .currency("PLN")
                    .build()

        when: "Adding new price"
            def request = mockMvc.perform(post("/gas-station/gas-price")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addPetrolPriceCommand)))

        then:
            def resultJson = request.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString()
            String resultUuid = TestUtils.extractContent(resultJson)

        when: "accepting new price"
            def uuid = UUID.fromString(resultUuid)
            def acceptCommand = AcceptPetrolPriceDto.builder().gasStationIdDto(GAS_STATION_ID_DTO).priceUuid(uuid).build()
            def nextRequest = mockMvc.perform(post("/gas-station/gas-price/accept")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(acceptCommand)))

        then:
            nextRequest.andExpect(status().isOk())

        when: "querying for price"
            def queryPrice = mockMvc.perform(post("/gas-station/gas-price/current")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(query)))

        then:
            queryPrice
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.content').value(DEFAULT_PRICE))
    }
}
