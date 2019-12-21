package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import com.splitoil.gasstation.dto.AddGasStationToObservableInputDto
import com.splitoil.gasstation.dto.GeoPointInputDto
import com.splitoil.gasstation.dto.ObservedGasStationOutputDto
import org.assertj.core.api.Assertions
import org.junit.experimental.categories.Category
import spock.lang.Specification

import java.util.stream.Collectors

@Category(UnitTest)
class GasStationsTest extends Specification {

    //@PendingFeature
    //@IgnoreIf
    //@Requires
    //@Retry
    //@Stepwise
    //@Title("This is easy to read")
    //@Narrative("""
    //As a user
    //I want foo
    //So that bar
    //""")
    //@See("http://spockframework.org/spec")
    //@Issue("http://my.issues.org/FOO-1")

    public static final BigDecimal LONGITUDE = -75.56
    public static final BigDecimal LATITUDE = 14.54
    public static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    public static final long DRIVER_ID = 1L
    private Driver driver
    private GasStationId gasStation
    private GasStationsFacade gasStationsFacade

    def setup() {
        gasStationsFacade = new GasStationConfiguration().gasStationsFacade()

        given:
            gasStation = new GasStationId(new GeoPoint(lat: LATITUDE, lon: LONGITUDE), GAS_STATION_NAME)

        and: "a driver"
            driver = new Driver(driverId: DRIVER_ID)
    }

    def "should add gas station to observed"() {
        given:
            def command = new AddGasStationToObservableInputDto(new GeoPointInputDto(LONGITUDE, LATITUDE), GAS_STATION_NAME, DRIVER_ID)

        when: "driver observes a gas station"
            gasStationsFacade.addToObservables(command)

        then: "driver sees gas station in observables"
            def expected = ObservedGasStationOutputDto.builder()
                    .location(new GeoPointInputDto(LONGITUDE, LATITUDE))
                    .name(GAS_STATION_NAME).build()
            def stations = gasStationsFacade.getObservedGasStations(DRIVER_ID)
            Assertions.assertThat(stations).contains(expected)
    }

    def "driver should rate gas station"() {
        when: "gas station has a new rating added"
            gasStationsFacade.rateGasStation(gasStation, new Rating(rating: 5))

        then: "have changed its rate value"
            gasStationsFacade.getRating(gasStation) == new BigDecimal(5)
    }

    def "adding second rate should create average of rates"() {
        when: "gas station has a new rating added"
            gasStationsFacade.rateGasStation(gasStation, new Rating(rating: 5))
            gasStationsFacade.rateGasStation(gasStation, new Rating(rating: 2))

        then: "have changed its rate value"
            gasStationsFacade.getRating(gasStation) == new BigDecimal("3.5")
    }

    def "driver should add petrol price to gas station"() {
        given: "petrol price"
            def petrolPrice = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)

        when: "driver adds new petrol price"
            gasStationsFacade.addPetrolPrice(gasStation, petrolPrice)

        and: "petrol price is accepted"
            gasStationsFacade.acceptPetrolPrice(gasStation, petrolPrice)

        then: "gas station has new price set in pending status"
            gasStationsFacade.getCurrentPetrolPrice(gasStation, PetrolType.BENZINE_95, Currency.PLN) == new BigDecimal("5.15")
    }

    def "driver should get petrol price equal to zero when no petrol price was added"() {
        expect: "price is zero bcuz no one added the price"
            gasStationsFacade.getCurrentPetrolPrice(gasStation, PetrolType.BENZINE_95, Currency.USD) == BigDecimal.ZERO
    }

    def "no one should see pending petrol price"() {
        given: "petrol price"
            def petrolPrice = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)

        when: "driver adds new petrol price"
            gasStationsFacade.addPetrolPrice(gasStation, petrolPrice)

        then: "new price cant be seen"
            gasStationsFacade.getCurrentPetrolPrice(gasStation, PetrolType.BENZINE_95, Currency.PLN) == BigDecimal.ZERO
    }

    //TODO: added by driver
    def "driver should get price equal to zero if no one provided in this currency"() {
        given: "petrol price"
            def petrolPrice = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)

        when: "driver adds new petrol price"
            gasStationsFacade.addPetrolPrice(gasStation, petrolPrice)

        and: "system accepts new price in PLN currency"
            gasStationsFacade.acceptPetrolPrice(gasStation, petrolPrice)

        then: "No price shown"
            gasStationsFacade.getCurrentPetrolPrice(gasStation, PetrolType.BENZINE_95, Currency.USD) == BigDecimal.ZERO
    }

    def "driver should get price equal to zero if no one provided price with this petrol type"() {
        given: "petrol price"
            def petrolPrice = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)

        when: "driver adds new petrol price"
            gasStationsFacade.addPetrolPrice(gasStation, petrolPrice)

        and: "system accepts new BENZINE_95 price in PLN currency"
            gasStationsFacade.acceptPetrolPrice(gasStation, petrolPrice)

        then: "No price shown"
            gasStationsFacade.getCurrentPetrolPrice(gasStation, PetrolType.OIL, Currency.PLN) == BigDecimal.ZERO
    }

    //todo: w facade DTOsy i dodaj service
    def "should show unrated gas station when no rate was added view"() {
        when: "checks petrol station"
            def gasStationBrief = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN);

        then: "No rate shown"
            gasStationBrief.stationRate == BigDecimal.ZERO
    }

    def "should show no prices when none was added view"() {
        when: "checks petrol station"
            def gasStationBrief = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN)

        then: "No prices shown"
            gasStationBrief.getPetrolPrices().get(0).getValue() == BigDecimal.ZERO
            gasStationBrief.getPetrolPrices().get(1).getValue() == BigDecimal.ZERO
            gasStationBrief.getPetrolPrices().get(2).getValue() == BigDecimal.ZERO
            gasStationBrief.getPetrolPrices().get(3).getValue() == BigDecimal.ZERO
    }

    def "should show rates at gas station"() {
        given:
            gasStationsFacade.rateGasStation(gasStation, Rating.of(4))

        when: "checks petrol station"
            def gasStationBrief = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN)

        then: "No rate shown"
            gasStationBrief.stationRate == new BigDecimal("4").setScale(2, GasStation.ROUNDING_MODE)
    }

    def "should show gas station prices"() {
        given:
            def benzine95 = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)
            def benzine98 = PetrolPrice.of(new BigDecimal("5.30"), Currency.PLN, PetrolType.BENZINE_98)
            def oil = PetrolPrice.of(new BigDecimal("4.89"), Currency.PLN, PetrolType.OIL)
            def lpg = PetrolPrice.of(new BigDecimal("3.15"), Currency.PLN, PetrolType.LPG)
            gasStationsFacade.addPetrolPrice(gasStation, benzine95)
            gasStationsFacade.addPetrolPrice(gasStation, benzine98)
            gasStationsFacade.addPetrolPrice(gasStation, oil)
            gasStationsFacade.addPetrolPrice(gasStation, lpg)

            gasStationsFacade.acceptPetrolPrice(gasStation, benzine95)
            gasStationsFacade.acceptPetrolPrice(gasStation, benzine98)
            gasStationsFacade.acceptPetrolPrice(gasStation, oil)
            gasStationsFacade.acceptPetrolPrice(gasStation, lpg)

        when: "checks petrol station"
            def pricesList = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN).getPetrolPrices().stream()
                    .map({ e -> e.getValue() })
                    .collect(Collectors.toList())

        then: "No prices shown"
            pricesList.contains(benzine95.getPetrolPrice())
            pricesList.contains(benzine98.getPetrolPrice())
            pricesList.contains(oil.getPetrolPrice())
            pricesList.contains(lpg.getPetrolPrice())
    }

    def "should show newest gas station price"() {
        given:
            def benzine95 = PetrolPrice.of(new BigDecimal("5.15"), Currency.PLN, PetrolType.BENZINE_95)
            def benzine95_2 = PetrolPrice.of(new BigDecimal("5.30"), Currency.PLN, PetrolType.BENZINE_95)
            gasStationsFacade.addPetrolPrice(gasStation, benzine95)
            gasStationsFacade.addPetrolPrice(gasStation, benzine95_2)

            gasStationsFacade.acceptPetrolPrice(gasStation, benzine95)
            gasStationsFacade.acceptPetrolPrice(gasStation, benzine95_2)

        when: "checks petrol station"
            def pricesList = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN).getPetrolPrices().stream()
                    .filter({e -> (e.petrolType == "BENZINE_95") })
                    .map({ e -> e.value })
                    .collect(Collectors.toList())

        then: "No prices shown"
            pricesList.contains(benzine95_2.getPetrolPrice())
    }

}
