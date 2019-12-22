Feature: Managing finishing the travel

  Scenario Main driver ends travel
    Given a active route
    When car arrives to destination place
    And driver enters the fuel level
    And accepts end travel
    Then travel is over

  Scenario Main driver stops the travel before destination point
    Given a active route
    When main driver stops the travel
    And main driver ends travel
    Then on point waypoint is added to history as destination point
    And travel is over

  Scenario Main driver rejects travel before destination point
    Given a active route
    When main driver stops the travel
    And main driver rejects travel
    Then all history is deleted
    And travel is deleted

  Scenario Main driver stops travel for a while
    Given a active route
    When main driver stops the travel
    And main driver resumes travel somewhere
    Then stop waypoint is added
    And resume waypoint is added
    And route is calculated
    And travel is active
