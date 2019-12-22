package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import com.splitoil.gasstation.dto.AcceptPetrolPriceDto
import com.splitoil.gasstation.dto.AddPetrolPriceDto
import com.splitoil.gasstation.dto.GasStationIdDto
import com.splitoil.gasstation.dto.GeoPointDto
import org.junit.experimental.categories.Category
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

@Category(UnitTest)
class GasStationsEdgeCaseTest extends Specification {

    private GasStationId gasStation
    private GasStationsFacade gasStationsFacade

    public static final double LONGITUDE = -75.56
    public static final double LATITUDE = 14.54
    public static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    public static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), GAS_STATION_NAME)

    def setup() {
        gasStationsFacade = new GasStationConfiguration().gasStationsFacade()
        given:
            gasStation = new GasStationId(new GeoPoint(lat: LATITUDE, lon: LONGITUDE), GAS_STATION_NAME)
    }

    def "should throw when petrol price was not found in pending prices list"() {
        given:
            def addPetrolPriceDto = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()
            def priceUuid = gasStationsFacade.addPetrolPrice(addPetrolPriceDto)
            def acceptCommand = AcceptPetrolPriceDto.builder().gasStationIdDto(GAS_STATION_ID_DTO).priceUuid(priceUuid).build()

            gasStationsFacade.acceptPetrolPrice(acceptCommand)

            def wrongAcceptCommand = AcceptPetrolPriceDto.builder().gasStationIdDto(GAS_STATION_ID_DTO).priceUuid(UUID.randomUUID()).build()

        when:
            gasStationsFacade.acceptPetrolPrice(wrongAcceptCommand)

        then:
            thrown(GasPriceNotFoundException)
    }

    def "should throw while accepting price when gas station doesnt exist"() {
        given:
            def acceptCommand = AcceptPetrolPriceDto.builder()
                    .gasStationIdDto(GasStationIdDto.of(GeoPointDto.of(200, 100), "Another name"))
                    .priceUuid(UUID.randomUUID())
                    .build()

        when:
            gasStationsFacade.acceptPetrolPrice(acceptCommand)

        then:
            thrown(EntityNotFoundException)
    }

}
