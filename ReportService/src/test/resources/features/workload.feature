Feature: Workload

  Scenario: Successful processing of workload addition requests from ActiveMQ
    Given the 5 workload requests in the ActiveMQ queue
    When the WorkloadReceiver processes additional requests
    Then 5 workloads should be sent to the MongoDB database
    And the workload of trainer "Sidor.Sidorov" should exist in the database

  Scenario: Failed processing of workload deletion requests from ActiveMQ
    Given the 5 workload requests in the ActiveMQ queue
    When the WorkloadReceiver processes deletion requests that will fail
    Then the workload of trainer "Sidor.Sidorov" should exist in the database

  Scenario: Successful processing of workload deletion requests from ActiveMQ
    Given the 5 workload requests in the ActiveMQ queue
    When the WorkloadReceiver processes deletion requests
    Then the workload of trainer "Sidor.Sidorov" should not exist in the database

  Scenario: Failed processing of workload addition requests from ActiveMQ
    Given the 5 workload requests in the ActiveMQ queue
    When the WorkloadReceiver processes additional requests that will fail
    And the workload of trainer "Sidor.Sidorov" should not exist in the database