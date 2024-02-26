Feature: Login
    Scenario: Successful login
        Given a user with username "Ivan.Ivanov" and password "password02"
        When the user tries to login
        Then the login should be successful

      Scenario: Unsuccessful login with wrong password
        Given a user with username "Ivan.Ivanov" and password "wrongpassword"
        When the user tries to login
        Then the login should be unsuccessful

      Scenario: Unsuccessful login with non-existent username
        Given a user with username "nonexistent.user" and password "password01"
        When the user tries to login
        Then the login should be unsuccessful