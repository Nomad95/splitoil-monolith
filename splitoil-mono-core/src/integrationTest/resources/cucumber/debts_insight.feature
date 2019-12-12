Feature: Passenger travel debts insights

  Scenario Passenger pays for a travel
    Given a finished travel
    And a notification about travel cost
    When travel is paid by passenger
    And passenger notifies driver
    Then appropriate driver gets notification about payment
