Feature: Trainee

  Scenario: Successful trainee registration
    Given a user with name "Ee", surname "eE, going to register as trainee"
    When the user tries to register
    Then the registration should be successful, trainee with username "Ee.eE" with generated password of length 10 was added to database

  Scenario: Successful trainee registration with existing username
    Given a user with name "Ee", surname "eE", going to register as trainee
    When the user tries to register
    Then the registration should be successful, trainee with username "Ee.eE1" with generated password of length 10 was added to database

  Scenario: Successful trainee password change
    Given a registered trainee with username from previous test
    When the trainee tries to change their password to "newpassword"
    Then the password change should be successful

  Scenario: Unsuccessful trainee password change due to wrong old password
    Given a registered trainee with username from previous test
    When the trainee tries to change their password to "wrongpassword" using wrong old password
    Then the password change should be unsuccessful

  Scenario: Successful trainee deactivation
    Given an active trainee with username from previous test
    When the admin tries to deactivate the trainee
    Then the deactivation should be successful

  Scenario: Unsuccessful trainee deactivation by another trainee
    Given a trainee tryes to change active to another trainee
    When the trainee tries to deactivate another trainee
    Then the deactivation should be unsuccessful

  Scenario: Successful trainee activation
    Given a registered trainee with username from previous test
    When the admin tries to activate the trainee
    Then the activation should be successful

  Scenario: Successful trainee profile update
    Given a registered trainee with username from previous test
    When the trainee tries to update their profile
    Then the trainee profile update should be successful

  Scenario: Successful trainee profile selection by username
    Given a registered trainee with username from previous test
    When the trainee tries to select their profile by username
    Then the trainee profile selection should be successful

  Scenario: Successful trainee trainings list retrieval
    Given a registered trainee with username from previous test
    When the trainee tries to get their trainings list
    Then the trainings list retrieval should be successful

  Scenario: Unsuccessful trainee profile update with invalid username
    Given a registered trainee with username from previous test
    When the trainee tries to update their profile with invalid username
    Then the profile update should be unsuccessful

  Scenario: Unsuccessful trainee profile selection by invalid username
    Given a registered trainee with username from previous test
    When the trainee tries to select their profile by invalid username
    Then the profile selection should be unsuccessful

  Scenario: Unsuccessful trainee trainings list retrieval with invalid username
    Given a registered trainee with username from previous test
    When the trainee tries to get their trainings list with invalid username
    Then the trainings list retrieval should be unsuccessful

  Scenario: Successful trainee profile deletion
    Given a registered trainee with username from previous test
    When the trainee tries to delete their profile
    Then the profile deletion should be successful

  Scenario: Unsuccessful trainee profile deletion with invalid username
    Given a registered trainee with username from previous test
    When the trainee tries to delete their profile with invalid username
    Then the profile deletion should be unsuccessful