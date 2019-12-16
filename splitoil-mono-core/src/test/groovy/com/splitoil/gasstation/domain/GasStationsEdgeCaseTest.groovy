package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import com.splitoil.gasstation.domain.*
import org.junit.experimental.categories.Category
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

@Category(UnitTest)
class GasStationsEdgeCaseTest extends Specification {

    private GasStationId gasStation
    private GasStationsFacade gasStationsFacade

    def setup() {
        gasStationsFacade = new GasStationConfiguration().gasStationsFacade()
        given:
            gasStation = new GasStationId(new GeoPoint(lat: 14.54, lon: -75.56), "Orlen Radziwiłłów 3")
    }

    def "should throw when petrol price was not found in pending prices list"() {
        given:
            def firstPrice = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)
            gasStationsFacade.addPetrolPrice(gasStation, firstPrice)
            gasStationsFacade.acceptPetrolPrice(gasStation, firstPrice)

            def wrongPriceToAccept = PetrolPrice.of(new BigDecimal("5.50"), Currency.PLN, PetrolType.BENZINE_95)

        when:
            gasStationsFacade.acceptPetrolPrice(gasStation, wrongPriceToAccept)

        then:
            thrown(GasPriceNotFoundException)
    }

    def "should throw while accepting price when gas station doesnt exist"() {
        given:
            def wrongPriceToAccept = PetrolPrice.of(new BigDecimal("5.50"), Currency.PLN, PetrolType.BENZINE_95)
            def wrongGasStation = new GasStationId(new GeoPoint(lat: 0.1, lon: -1.1), "another gas sation")

        when:
            gasStationsFacade.acceptPetrolPrice(wrongGasStation, wrongPriceToAccept)

        then:
            thrown(EntityNotFoundException)
    }

}
