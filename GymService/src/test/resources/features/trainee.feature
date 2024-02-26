Feature: Trainee

  Scenario: Successful trainee registration
    Given a user with name "Ee", surname "eE"
    When the user tries to register
    Then the registration should be successful, trainee with username "Ee.eE" with generated password of length 10 was added to database

  Scenario: Successful trainee registration with existing username
    Given a user with name "Ee", surname "eE"
    When the user tries to register
    Then the registration should be successful, trainee with username "Ee.eE1" with generated password of length 10 was added to database

  Scenario: Successful trainee password change
    Given a registered trainee with username from previous test
    When the trainee tries to change their password to "newpassword"
    Then the password change should be successful