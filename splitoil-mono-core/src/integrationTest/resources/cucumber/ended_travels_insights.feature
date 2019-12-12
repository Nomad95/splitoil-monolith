Feature: Ended travels insight for driver

  Scenario Driver wants to have his costs paid
    Given a finished travel
    When all payments were received
    And all payments were confirmed
    Then payment process is done
    And all payments marked as done

  Scenario Driver wants to cancel passenger's payment
    Given a finished travel
    When driver cancels passenger's debt
    Then passenger is notified
    And payment is marked as no longer valid

  Scenario Driver wants to cancel the travel
    Given a finished travel
    When driver cancels the travel
    Then passengers are notified
    And all travel payments are cancelled

  Scenario Driver wants to notify user about pending payment
    Given a finished travel
    When driver notifies passenger about pending payment
    Then passenger is notified
    And driver cannot notify passenger again
    When one day passes
    And passengers have not paid
    Then driver can send notification again