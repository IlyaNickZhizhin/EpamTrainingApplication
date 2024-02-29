Feature: Trainer
  Scenario: Successful trainer registration
    Given a user with name "Er", surname "eR", specialization "BASIC"
    When the user tries to register as a trainer
    Then the registration should be successful, trainer with username "Er.eR" with generated password of length 10 was added to database

  Scenario: Unsuccessful trainer registration with missing data
    Given a user with name "Er", surname "eR", specialization ""
    When the user tries to register as a trainer
    Then the registration should fail with a "400 BAD_REQUEST" error

  Scenario: Successful trainer password change
    Given a registered trainer with username from previous test
    When the trainer tries to change their password to "newpassword"
    Then trainers password change should be successful

  Scenario: Unsuccessful trainer password change with wrong old password
    Given a registered trainer with username from previous test
    When the trainer tries to change their password to "wrongpassword" using wrong old password
    Then trainers password change should be unsuccessful

  Scenario: Successful trainer profile update
    Given a registered trainer with username from previous test
    When the trainer tries to update their profile with name "Er", surname "eRRR", specialization "BASIC"
    Then the trainer profile update should be successful with name "Er", surname "eRRR", specialization "BASIC"

  Scenario: Unsuccessful trainer profile update with invalid data
    Given a registered trainer with username from previous test
    When the trainer tries to update their profile with name "Er", surname "", specialization "BASIC"
    Then the trainer profile update should fail with a "400 BAD_REQUEST" error

  Scenario: Successful trainer profile selection by username
    Given a registered trainer with username from previous test
    When the trainer tries to select their profile by username
    Then the trainer profile selection should be successful

  Scenario: Unsuccessful trainer profile selection with non-existent username
    Given a registered trainer with username from previous test
    When the admin tries to select profile by username "username.notexist"
    Then the trainer profile selection should fail with a "400 BAD_REQUEST" error

  Scenario: Successful trainer trainings list retrieval
    Given a registered trainer with username from previous test
    When the trainer tries to get their trainings list
    Then trainers trainings list retrieval should be successful

  Scenario: Successful trainer deactivation
    Given a registered and activated trainer with username "Ivan.Ivanov"
    When tries to deactivate himself
    Then the trainer deactivation should be successful

  Scenario: Successful trainer activation
    Given a registered and deactivated trainer with username "Ivan.Ivanov"
    When tries to activate himself
    Then the trainer activation should be successful

  Scenario: Unsuccessful trainer activation with non-existent username
    Given a registered and activated trainer with username "Ivan.Ivanov"
    When as admin tries to activate the trainer with username "nonexistentuser"
    Then the trainer activation should fail with a "400 BAD_REQUEST" error

  Scenario: Unsuccessful trainer deactivation with non-existent username
    Given a registered and activated trainer with username "Ivan.Ivanov"
    When as admin tries to deactivate the trainer with username "nonexistentuser"
    Then the trainer deactivation should fail with a "400 BAD_REQUEST" error

