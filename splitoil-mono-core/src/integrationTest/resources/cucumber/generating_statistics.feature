Feature: Generate statistics after travel ends

  Scenario Generate statistics after travel ends
    Given saved travel
    Then statistics are generated
    And notifications are sent
