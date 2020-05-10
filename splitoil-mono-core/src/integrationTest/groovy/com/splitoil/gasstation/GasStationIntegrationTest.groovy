package com.splitoil.gasstation

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.gasstation.dto.*
import com.splitoil.infrastructure.security.WithMockSecurityContext
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

    @Sql(scripts = '/db/gas_station/default_observed_gas_station.sql')
    def "driver gets his own observed gas station list"() {
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

    def "Driver can rate gas station"() {
        given:
            def addRatingCommand = new AddRatingDto(GAS_STATION_ID_DTO, 4)

        when:
            def result = mockMvc.perform(post("/gas-station/rate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addRatingCommand)))

        then:
            result.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
    }

    @Sql(scripts = '/db/gas_station/gas_station_with_rating_4.sql')
    def "driver can see gas station rating"() {
        when:
            def ratings = mockMvc.perform(post("/gas-station/rating")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(GAS_STATION_ID_DTO)))

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

    //TODO: i think this should be done internally - scheduler or something
    @Sql(scripts = '/db/gas_station/default_petrol_price_not_accepted.sql')
    def "system should accept gas price"() {
        given:
            def uuid = UUID.fromString('e0cbe344-b095-4621-a1ce-69351175daab')
            def acceptCommand = AcceptPetrolPriceDto.builder().priceUuid(uuid).build()

        when:
            def nextRequest = mockMvc.perform(post("/gas-station/gas-price/accept")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(acceptCommand)))

        then:
            nextRequest.andExpect(status().isOk())
    }

    @Sql(scripts = '/db/gas_station/default_petrol_price.sql')
    def "should get gas price from gas station by type and currency"() {
        given:
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType("BENZINE_95")
                    .currency("PLN")
                    .build()

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
