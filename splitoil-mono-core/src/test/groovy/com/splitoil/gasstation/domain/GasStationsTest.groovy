package com.splitoil.gasstation.domain

import com.splitoil.UnitTest
import com.splitoil.gasstation.dto.*
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

    static final double LONGITUDE = -75.56
    static final double LATITUDE = 14.54
    static final String GAS_STATION_NAME = "Orlen Radziwiłłów 3"
    static final long DRIVER_ID = 1L
    static final GasStationIdDto GAS_STATION_ID_DTO = GasStationIdDto.of(GeoPointDto.of(LONGITUDE, LATITUDE), GAS_STATION_NAME)

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
            def gasStationIdDto = GAS_STATION_ID_DTO
            def driverDto = DriverDto.of(DRIVER_ID)
            def command = new AddToObservableDto(gasStationIdDto, driverDto)

        when: "driver observes a gas station"
            gasStationsFacade.addToObservables(command)

        then: "driver sees gas station in observables"
            def stations = gasStationsFacade.getObservedGasStations(DRIVER_ID)
            Assertions.assertThat(stations).contains(gasStationIdDto)
    }

    def "driver should rate gas station"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 5)

        when: "gas station has a new rating added"
            gasStationsFacade.rateGasStation(addRatingCommand)

        then: "have changed its rate value"
            gasStationsFacade.getRating(gasStationIdDto) == new BigDecimal(5)
    }

    def "adding second rate should create average of rates"() {
        given:
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 5)
            def addRatingCommand2 = new AddRatingDto(gasStationIdDto, 2)

        when: "gas station has a new rating added"
            gasStationsFacade.rateGasStation(addRatingCommand)
            gasStationsFacade.rateGasStation(addRatingCommand2)

        then: "have changed its rate value"
            gasStationsFacade.getRating(gasStationIdDto) == new BigDecimal("3.5")
    }

    def "driver should add petrol price to gas station"() {
        given: "petrol price"
            def addPetrolPriceDto = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()

        when: "driver adds new petrol price"
            def petrolPriceUuid = gasStationsFacade.addPetrolPrice(addPetrolPriceDto)
            def acceptCommand = defaultGasStationBuilder().priceUuid(petrolPriceUuid).build()

        and: "petrol price is accepted"
            gasStationsFacade.acceptPetrolPrice(acceptCommand)

        then: "gas station has new price set in pending status"
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType(PetrolType.BENZINE_95.name())
                    .currency(Currency.PLN.name())
                    .build()
            gasStationsFacade.getCurrentPetrolPrice(query) == new BigDecimal("5.15")
    }

    def "driver should get petrol price equal to zero when no petrol price was added"() {
        given:
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType(PetrolType.BENZINE_95.name())
                    .currency(Currency.PLN.name())
                    .build()
        expect: "price is zero bcuz no one added the price"
            gasStationsFacade.getCurrentPetrolPrice(query) == BigDecimal.ZERO
    }

    def "no one should see pending petrol price"() {
        given: "petrol price"
            def addPetrolPriceDto = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()

        when: "driver adds new petrol price"
            gasStationsFacade.addPetrolPrice(addPetrolPriceDto)

        then: "new price cant be seen"
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType(PetrolType.BENZINE_95.name())
                    .currency(Currency.PLN.name())
                    .build()
            gasStationsFacade.getCurrentPetrolPrice(query) == BigDecimal.ZERO
    }

    def "driver should get price equal to zero if no one provided in this currency"() {
        given: "petrol price"
            def addPetrolPriceDto = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()

        when: "driver adds new petrol price"
            def petrolPriceUuid = gasStationsFacade.addPetrolPrice(addPetrolPriceDto)
            def acceptCommand = defaultGasStationBuilder().priceUuid(petrolPriceUuid).build()

        and: "system accepts new price in PLN currency"
            gasStationsFacade.acceptPetrolPrice(acceptCommand)

        then: "No price shown"
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType(PetrolType.BENZINE_95.name())
                    .currency(Currency.USD.name())
                    .build()
            gasStationsFacade.getCurrentPetrolPrice(query) == BigDecimal.ZERO
    }

    def "driver should get price equal to zero if no one provided price with this petrol type"() {
        given: "petrol price"
            def addPetrolPriceDto = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()

        when: "driver adds new petrol price"
            def priceUuid = gasStationsFacade.addPetrolPrice(addPetrolPriceDto)
            def acceptCommand = defaultGasStationBuilder().priceUuid(priceUuid).build()

        and: "system accepts new BENZINE_95 price in PLN currency"
            gasStationsFacade.acceptPetrolPrice(acceptCommand)

        then: "No price shown"
            def query = GetPetrolPriceDto.builder()
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .petrolType(PetrolType.OIL.name())
                    .currency(Currency.PLN.name())
                    .build()
            gasStationsFacade.getCurrentPetrolPrice(query) == BigDecimal.ZERO
    }

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
            def gasStationIdDto = GAS_STATION_ID_DTO
            def addRatingCommand = new AddRatingDto(gasStationIdDto, 4)
            gasStationsFacade.rateGasStation(addRatingCommand)

        when: "checks petrol station"
            def gasStationBrief = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN)

        then: "No rate shown"
            gasStationBrief.stationRate == new BigDecimal("4").setScale(2, GasStation.ROUNDING_MODE)
    }

    def "should show gas station prices"() {
        given:
            def benzine95 = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()
            def benzine98 = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.30"))
                    .petrolType(PetrolType.BENZINE_98.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()
            def oil = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("4.89"))
                    .petrolType(PetrolType.OIL.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()
            def lpg = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("3.15"))
                    .petrolType(PetrolType.LPG.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()

            def benzine95PriceUuid = gasStationsFacade.addPetrolPrice(benzine95)
            def benzine98PriceUuid = gasStationsFacade.addPetrolPrice(benzine98)
            def oilPriceUuid = gasStationsFacade.addPetrolPrice(oil)
            def lpgPriceUuid = gasStationsFacade.addPetrolPrice(lpg)

            def acceptCommand95 = defaultGasStationBuilder().priceUuid(benzine95PriceUuid).build()
            def acceptCommand98 = defaultGasStationBuilder().priceUuid(benzine98PriceUuid).build()
            def acceptCommandOil = defaultGasStationBuilder().priceUuid(oilPriceUuid).build()
            def acceptCommandLpg = defaultGasStationBuilder().priceUuid(lpgPriceUuid).build()

            gasStationsFacade.acceptPetrolPrice(acceptCommand95)
            gasStationsFacade.acceptPetrolPrice(acceptCommand98)
            gasStationsFacade.acceptPetrolPrice(acceptCommandOil)
            gasStationsFacade.acceptPetrolPrice(acceptCommandLpg)

        when: "checks petrol station"
            def pricesList = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN).getPetrolPrices().stream()
                    .map({ e -> e.getValue() })
                    .collect(Collectors.toList())

        then: "Prices shown"
            pricesList.contains(benzine95.getAmount())
            pricesList.contains(benzine98.getAmount())
            pricesList.contains(oil.getAmount())
            pricesList.contains(lpg.getAmount())
    }

    def "should show newest gas station price"() {
        given:
            def benzine95 = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.15"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()
            def benzine95_2 = AddPetrolPriceDto.builder()
                    .currency(Currency.PLN.name())
                    .amount(new BigDecimal("5.30"))
                    .petrolType(PetrolType.BENZINE_95.name())
                    .gasStationIdDto(GAS_STATION_ID_DTO)
                    .build()
            def price2Uuid = gasStationsFacade.addPetrolPrice(benzine95_2) //This behaviour is bugged because i cant sort by backage scoped attribute
            def priceUUid = gasStationsFacade.addPetrolPrice(benzine95)
            def acceptCommand2 = defaultGasStationBuilder().priceUuid(price2Uuid).build()
            def acceptCommand = defaultGasStationBuilder().priceUuid(priceUUid).build()

            gasStationsFacade.acceptPetrolPrice(acceptCommand)
            gasStationsFacade.acceptPetrolPrice(acceptCommand2)

        when: "checks petrol station"
            def pricesList = gasStationsFacade.showGasStationBrief(gasStation, Currency.PLN).getPetrolPrices().stream()
                    .filter({ e -> (e.petrolType == "BENZINE_95") })
                    .map({ e -> e.value })
                    .collect(Collectors.toList())

        then: "Newest price shown"
            pricesList.contains(benzine95_2.getAmount())
    }

    private static AcceptPetrolPriceDto.AcceptPetrolPriceDtoBuilder defaultGasStationBuilder() {
        return AcceptPetrolPriceDto.builder()
    }

}
