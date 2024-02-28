Feature: Trainer
  Scenario: Successful trainer registration
    Given a user with name "Er", surname "eR", specialization "BASIC"
    When the user tries to register as a trainer
    Then the registration should be successful, trainer with username "Er.eR" with generated password of length 10 was added to database

  Scenario: Successful trainer password change
    Given a registered trainer with username from previous test
    When the trainer tries to change their password to "newpassword"
    Then the password change should be successful

  Scenario: Successful trainer profile update
    Given a registered trainer with username from previous test
    When the trainer tries to update their profile
    Then the trainer profile update should be successful

  Scenario: Successful trainer profile selection by username
    Given a registered trainer with username from previous test
    When the trainer tries to select their profile by username
    Then the trainer profile selection should be successful

  Scenario: Successful trainer trainings list retrieval
    Given a registered trainer with username from previous test
    When the trainer tries to get their trainings list
    Then the trainings list retrieval should be successful
