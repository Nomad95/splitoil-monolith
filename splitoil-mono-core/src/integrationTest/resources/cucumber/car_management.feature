Feature: Driver manages his car

  Scenario Driver adds a car into the system
    Given a empty car storage
    When I add my first car
    Then car is added into my car storage

  Scenario: Driver adds parameters to his car
    Given chosen car
    When driver adds initial fuel tank
    And driver edits the average fuel consumption
    And driver edits a car mileage
    Then driver can see his car brief information

  Scenario: Driver deletes his car
    Given chosen car
    When driver deletes car
    Then car is marked as deleted

  Scenario: Driver adds cost related to his car
    Given chosen car
    When driver adds a cost related to his car
    Then car has a cost added

  Scenario: Car was refueled
    Given chosen car
    When driver adds a refueling to this car
    Then car has a refuel event added

  Scenario: Car was refueled while travelling
    Given a refuel while travelling
    When system adds a refueling to this car
    Then car has a refuel event added

  Scenario: Car travelled 500km and fuel consumption can be calculated
    Given a travel that sum 500km
    When system calculates average fuel consumption
    Then average fuel consumption is calculated

  Scenario: Car travelled 500km and fuel average cost can be calculated
    Given a travel that sum 500km
    When system calculates average fuel cost per 1 KM
    Then average fuel cost is calculated

  Scenario: The travel has ended and system saves the information to particular car
    Given an ended travel
    When system processes the event
    Then car state was changed
