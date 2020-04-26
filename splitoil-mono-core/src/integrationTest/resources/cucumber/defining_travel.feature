Feature: Driver defines a travel route

  Scenario Main driver defines route
    When driver selects travel beginning
    Then travel has defined beginning place
    And is shown on map
    But route is not calculated yet
    When driver selects travel destination
    Then travel has defined destination place
    And is shown on map
    And route is calculated
    When driver selects a change place
    Then travel route has a change place defined
    And is shown on map
    And route is calculated
    When driver selects a refueling place
    Then travel route has a refueling place defined
    And is shown on map
    And route is calculated
    When driver selects a stop place
    Then travel route has a stop place defined
    And is shown on map
    And route is calculated
    When driver selects a passenger boarding place
    Then travel route has a passenger boarding place defined
    And is shown on map
    And route is calculated
    When driver selects a passenger exit place
    Then travel route has a exit place defined
    And is shown on map
    And route is calculated
    When driver selects an existing waypoint
    And moves it to another place
    Then travel route has changed one waypoint definition
    And is shown on map
    And route is calculated
    When driver selects an existing place
    And deletes it
    Then travel route waypoint is deleted
    And disappears on map
    And route is calculated
    When driver confirms all waypoints
    Then waypoints are confirmed
    And route is calculated

  Scenario: Main driver reorders waypoints
    Given a defined route in one order
    When driver reorders middle waypoint
    Then travel waypoints order has changed
    And route is recalculated

  Scenario: Main driver reorders waypoints while travelling
    Given a defined route
    And calculated route on map
    When driver reorders middle waypoint
    Then travel waypoints order has changed
    And route is recalculated

  Scenario: Main driver confirms route and starts travel
    Given a defined route
    And calculated route on map
    When driver confirms the travel plan
    And enters the fuel level
    And starts the travel
    Then travel is started
    And notification is sent to passengers
    And gps route calculation is started

  Scenario Driver configures road section cost
    Given a travel route
    When driver selects a section
    And adds a rate for section
    And selects another section
    And adds a rate for section
    Then travel has different costs for different sections
