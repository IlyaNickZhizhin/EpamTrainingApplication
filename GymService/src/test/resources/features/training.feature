Feature: Training

  Scenario: Successful creating a new training
    Given a active trainer with username "Petr.Petrov" and password "password01"
    When the user makes request to create "trainings/training1" (for himself)
    Then the response status should be "201 CREATED"
    And the response should contain a training with name "TEST BASIC"

  Scenario: Unsuccessful creating a new training
    Given a active trainer with username "Petr.Petrov" and password "password01"
    When the user makes request to create "trainings/training2" (not for himself)
    Then the response status should be "400 BAD_REQUEST"

  Scenario: Get all training types
    When the user makes request to all training types
    Then the getTrainingTypes response status should be "200 OK"
    And the response should contain all 12 training types witch contains in "trainingtypes/trainingType"

  Scenario: Update trainers list
    Given a active trainee with username "Pavel.Pavlov" and password "password06"
    When the user makes a PUT request to update his trainers list with trainer "Petr.Petrov"
    Then the updateTrainersList response status should be "200 OK"
    And the response should contain the list of trainers "trainers/trainer1|trainers/trainer2"

  Scenario: Unsuccessful update trainers list
    Given a active trainee with username "Sidor.Sidorov" and password "password03"
    When the user makes a PUT request to update "Pavel.Pavlov"s trainers list with trainer "Petr.Petrov"
    Then the updateTrainersList response status should be "400 BAD_REQUEST"

  Scenario: Successful update trainers list by admin
    Given a active admin with username "Sidor.Sidorov" and password "password03"
    When the user makes a PUT request to update "Pavel.Pavlov"s trainers list with trainer "Petr.Petrov"
    Then the updateTrainersList response status should be "200 OK"
    And the response should contain the list of trainers "trainers/trainer1|trainers/trainer2"

  Scenario: Get trainers list
    Given a active trainee with username "Sidor.Sidorov" and password "password03"
    When the user makes a GET request for getting all his trainers
    Then the getTrainers response status should be "200 OK"
    And the response should contain the list of trainers "trainers/trainer1|trainers/trainer2"

  Scenario: Unsuccessful getting trainers list
    Given a active trainee with username "Pavel.Pavlov" and password "password06"
    When the user makes a GET request for getting all "Sidor.Sidorov"s trainers
    Then the getTrainers response status should be "400 BAD_REQUEST"

  Scenario: Successful get trainers list by admin
    Given a active admin with username "Pavel.Pavlov" and password "password06"
    When the user makes a GET request for getting all "Sidor.Sidorov"s trainers
    Then the getTrainers response status should be "200 OK"
    And the response should contain the list of trainers "trainers/trainer1|trainers/trainer2"

  Scenario: Unsuccessful getting not assigned active trainers
    Given a active trainee with username "Pavel.Pavlov" and password "password06"
    When the user makes a GET request for getting all available trainers for "Semen.Semenov"
    Then the getTrainers response status should be "400 BAD_REQUEST"

  Scenario: Successful get not assigned active trainers by admin
    Given a active admin with username "Pavel.Pavlov" and password "password06"
    When the user makes a GET request for getting all available trainers for "Semen.Semenov"
    Then the getTrainers response status should be "200 OK"
    And the response should contain the list of trainers "trainers/trainer1"

  Scenario: Get not assigned active trainers
    Given a active trainee with username "Semen.Semenov" and password "password05"
    When the user makes a GET request for getting all available trainers
    Then the getTrainers response status should be "200 OK"
    And the response should contain the list of trainers "trainers/trainer1"
