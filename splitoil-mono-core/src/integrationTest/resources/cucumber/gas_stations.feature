Feature: Gas station management

  Scenario Driver marks his gas station as observed
    Given a gas station on map
    When driver observe a gas station
    Then driver sees gas station in observables

  Scenario Driver rates gas station
    Given a gas station on map
    When driver rates a gas station
    Then gas station has a new rating added
    And have changed its rate value

  Scenario Driver adds petrol price on gas station
    Given a gas station on map
    When driver adds fresh new petrol price
    Then gas station has new pending price
