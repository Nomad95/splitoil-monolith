package com.splitoil.car

import com.splitoil.IntegrationTest
import com.splitoil.base.IntegrationSpec
import com.splitoil.car.dto.*
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(IntegrationTest)
class CarIntegrationTest extends IntegrationSpec {

    public static final DriverDto DRIVER_DTO = DriverDto.of(1L)
    public static final AddCarDto CAR_INPUT_DTO = AddCarDto.builder().name("A4").brand("Audi").driver(DRIVER_DTO).build()
    static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(100, 200), "Station")
    static final Instant NOW = Instant.now()

    def "Driver adds a car into system"() {
        when: "I add my first car"
            def result = mockMvc.perform(post("/car")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(CAR_INPUT_DTO)))

        then: "is created"
            result
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
    }


    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver gets all his car list"() {
        when: "driver gets his cars list"
            def listResult = mockMvc.perform(get("/car/driver/" + DRIVER_DTO.getId())
                    .contentType(MediaType.APPLICATION_JSON))

        then: "drivers cars are present"
            listResult
                    .andExpect(jsonPath('$._embedded.carViewList.[0].id').value(1L))
                    .andExpect(jsonPath('$._embedded.carViewList.[0].name').value("A4"))
                    .andExpect(jsonPath('$._embedded.carViewList.[0].brand').value("Audi"))
                    .andExpect(jsonPath('$._embedded.carViewList.[0].driverId').value(1))
    }


    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver deletes his car"() {
        when: "driver deletes his car"
            def deleteResult = mockMvc.perform(delete("/car/" + 1)
                    .contentType(MediaType.APPLICATION_JSON))

        then: "is deleted"
            deleteResult
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isNoContent())
    }

    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver adds initial mileage to his car"() {
        given:
             def addMileageDto = AddCarMileageDto.builder()
                    .carId(1L)
                    .mileage(150_000)
                    .build()

        when: "driver adds initial mileage to his car"
            def addResult = mockMvc.perform(post("/car/initial-mileage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addMileageDto)))

        then:
            addResult
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.id').value(1))
                    .andExpect(jsonPath('$.mileage').value(150_000))

    }

    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver defines his car's fuel tank"() {
        given:
            def addCarTank = FuelTankDto.builder()
                    .carId(1L)
                    .capacity(new BigDecimal("65"))
                    .fuelType("BENZINE_95")
                    .build()

        when: "driver defines fuel tank"
            def addResult = mockMvc.perform(post("/car/fuel-tank")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addCarTank)))

        then:
            addResult
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.id').value(1))
                    .andExpect(jsonPath('$.fuelCapacity').value("65.0"))
    }

    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver defines his car's avg fuel consumption"() {
        given:
            def fuelConsumption = AddCarAverageFuelConsumptionDto.builder()
                    .carId(1L)
                    .avgFuelConsumption(new BigDecimal("11.5"))
                    .build()

        when: "driver defines avg fuel consumption"
            def addResult = mockMvc.perform(post("/car/avg-fuel-consumption")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(fuelConsumption)))

        then:
            addResult
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.id').value(1))
                    .andExpect(jsonPath('$.avgFuelConsumption').value("11.5"))
    }

    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver adds cost to his car"() {
       given:
            def addCostDto = AddCarCostDto.builder()
                    .carId(1l)
                    .value(new BigDecimal("125.53")) //TODO: currency
                    .name("Window repair")
                    .build()
            def addCostDto2 = AddCarCostDto.builder()
                    .carId(1L)
                    .value(new BigDecimal("1000.0"))
                    .name("Engine repair")
                    .build()

        when: "driver defines avg fuel consumption"
            mockMvc.perform(post("/car/cost")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addCostDto)))
            mockMvc.perform(post("/car/cost")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(addCostDto2)))

            def overallCostResult = mockMvc.perform(get("/car/{id}/cost", 1)
                    .contentType(MediaType.APPLICATION_JSON))

        then:
            overallCostResult
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.content').value("1125.53"))
    }

    @Sql(scripts = '/db/car/default_car.sql')
    def "Driver refuels his car and enters its info to system"() {
        given:
            def refuelCommand = RefuelCarDto.builder()
                    .carId(1L)
                    .petrolType("BENZINE_95")
                    .amount(new BigDecimal("25.0"))
                    .gasStation(GAS_STATION_ID_DTO)
                    .cost(new BigDecimal("100.0"))
                    .date(NOW)
                    .build()

        when: "driver adds new refuel manually"
            mockMvc.perform(post("/car/refuel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jackson.toJson(refuelCommand))
                    .param("page", "0")
                    .param("size", "20"))

            def refuels = mockMvc.perform(get("/car/{id}/refuel", 1)
                    .contentType(MediaType.APPLICATION_JSON))

        then:
            refuels
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath('$._embedded.refuelCarOutputDtoList.[0].amount').value("25.0"))
                    .andExpect(jsonPath('$._embedded.refuelCarOutputDtoList.[0].cost').value("100.0"))
                    .andExpect(jsonPath('$._embedded.refuelCarOutputDtoList.[0].petrolType').value("BENZINE_95"))
    }

    @Sql(scripts = '/db/car/default_car.sql')
    def "Should get one car"() {
        when:
            def car = mockMvc.perform(get("/car/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON))

        then:
            car
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.id').value(1))
                    .andExpect(jsonPath('$.brand').value("Audi"))
    }

}
