Feature: Gas station management

  Scenario: Driver marks his gas station as observed
    Given a gas station on map
    And a driver
    When driver observe a gas station
    Then driver sees gas station in observables

  Scenario: Driver rates gas station
    Given a gas station on map
    When driver rates a gas station
    Then gas station has a new rating added
    And have changed its rate value

  Scenario: Driver adds petrol price on gas station
    Given a gas station on map
    When driver adds fresh new petrol price
    Then gas station has new pending price

  Scenario: Driver checks petrol station on map and sees no rates
    Given a gas station on map
    And driver
    When driver checks out gas station without any rates
    Then gas station is shown as unrated

  Scenario: Driver checks petrol station on map and sees no prices
    Given a gas station on map
    And driver
    When driver checks out gas station without any prices
    Then gas station is shown as without defined price

  Scenario: Driver adds first rate
    Given a gas station on map
    And driver
    When driver adds new rate to gas station
    Then gas station shows rating

  Scenario: Driver adds first petrol price
    Given a gas station on map
    And driver
    When driver adds new price to gas station
    And price is correct
    And price is accepted
    Then gas station shows prices