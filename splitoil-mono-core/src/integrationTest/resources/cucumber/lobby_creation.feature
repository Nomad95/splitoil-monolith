Feature: Driver with car can create travel by defining a lobby

  Scenario Driver creates lobby for new travel
    Given driver has a car
    And driver does not have another lobby created simultaneously
    When driver creates new lobby
    And have chosen a car
    Then lobby has been created

  Scenario Driver configures lobby for new travel
    Given a new lobby
    When driver sets top rate per 1 KM
    And adds another driver with car
    And defines the travel currency
    Then lobby has been configured

  Scenario Driver configures road section cost
    Given a travel route
    When driver selects a section
    And adds a rate for section
    And selects another section
    And adds a rate for section
    Then travel has different costs for different sections

  Scenario Driver manages passengers
    Given a new lobby
    When driver adds a passenger
    And passenger has accepted invitation
    Then passenger is joined to the lobby
    When driver adds a external passenger
    Then passenger is joined to the lobby
    But is only a temporal passenger
    When driver disables passenger from cost charging
    Then passengers is not taken into account in travel costs
    When driver changes passengers currency
    Then passenger is marked to have another currency
    When driver assigns user to car
    And car has a free space
    Then user is assigned to a car
    When driver deletes passenger from lobby
    Then passenger is no longer in lobby
