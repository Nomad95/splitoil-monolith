Feature: System gathers information about current travel

  Scenario Travel contains all possible waypoints
    Given a travel with every waypoint
    When car reaches refuel waypoint
    Then waypoint is marked as historical
    And car has added a refuel cost
    And adds waypoint to history line
    When car reaches passenger boarding waypoint
    Then waypoint is marked as historical
    And adds waypoint to history line
    When car reaches passenger exit place waypoint
    Then waypoint is marked as historical
    And adds waypoint to history line
    When car reaches stop place waypoint
    Then waypoint is marked as historical
    And travel is in stop state
    And adds waypoint to history line
    When car reaches change waypoint
    Then waypoint is marked as historical
    And adds waypoint to history line
    When gps signal is interrupted
    And then signal returns
    Then route is interpolated
    And route is recalculated

    