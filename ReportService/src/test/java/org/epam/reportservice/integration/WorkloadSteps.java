package org.epam.reportservice.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.epam.common.dto.MonthDuration;
import org.epam.reportservice.Reader;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.receiver.JmsWorkloadReceiver;
import org.epam.reportservice.repository.mongo.WorkloadRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@CucumberContextConfiguration
@SpringBootTest
public class WorkloadSteps {

    @Autowired
    private JmsWorkloadReceiver receiver;
    @Autowired
    private WorkloadRepositoryMongo repository;
    @Autowired
    private JmsTemplate jmsTemplate;

    Reader reader = new Reader();

    private List<ReportTrainerWorkloadRequest> requests = new ArrayList<>();
    private ReportTrainerWorkloadResponse response;


    @Given("the {int} workload requests in the ActiveMQ queue")
    public void createListOfRequests(int messageCount) {
        for (int i=1; i<=messageCount; i++){
            ReportTrainerWorkloadRequest request =
                    reader.readEntity("src/test/resources/models/workloads/workload"+i+".json",
                            ReportTrainerWorkloadRequest.class);
            requests.add(request);
        }
    }

    @When("the WorkloadReceiver processes additional requests")
    public void sendAllAddRequests() {
        for(ReportTrainerWorkloadRequest request : requests) {
            receiver.receiveAddMessage(request);
        }
    }

    @When("the WorkloadReceiver processes additional requests that will fail")
    public void sendAllAddRequestsFail() {
        for(ReportTrainerWorkloadRequest request : requests) {
            jmsTemplate.convertAndSend("DLQ.addTrainingRequestQueue", request);
        }
    }

    @When("the WorkloadReceiver processes deletion requests")
    public void sendAllDeleteRequests() {
        for(ReportTrainerWorkloadRequest request : requests) {
            receiver.receiveDeleteMessage(request);
        }
    }

    @When("the WorkloadReceiver processes deletion requests that will fail")
    public void sendAllDeleteRequestsFail() {
        for(ReportTrainerWorkloadRequest request : requests) {
            jmsTemplate.convertAndSend("DLQ.deleteTrainingRequestQueue", request);
        }
    }
    @Then("{int} workloads should be sent to the MongoDB database")
    public void checkReceivingOfMongo(int count) {
        assertEquals(count-1, (long) repository.findAll().size());
    }

    @Then("the workload of trainer {string} should not exist in the database")
    public void checkEmptyWorkloadOfMongo(String username) {
        if (repository.existsById(username)) {
            double duration =
                    repository.findById(username).get().getTrainingSessions().stream()
                            .flatMap(ts -> ts.getMonths().stream())
                            .mapToDouble(MonthDuration::getDuration).sum();
            assertEquals(0.0, duration);
        }
    }

    @And("the workload of trainer {string} should exist in the database")
    public void checkContainOfMongo(String username) {
        assertTrue(repository.existsById(username));
    }

}
