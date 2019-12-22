Feature: Main driver ends travel and has a summary

  Scenario Main driver saves travel
    Given ended travel
    When driver selects to allocate costs automatically
    And accepts it
    And saves travel
    Then travel is saved
    And costs are saved

  Scenario Main driver allocates costa manually
    Given ended travel
    When driver selects to allocate costs manually
    And allocates manually
    And saves travel
    Then travel is saved
    And costs are saved
