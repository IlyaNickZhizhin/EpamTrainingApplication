package org.epam.reportservice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;


@DynamoDBTable(tableName = "")
@Data
public class WorkloadDynamo {

    @Id
    @DynamoDBHashKey(attributeName = "username")
    private String username;
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "username-index", attributeName = "trainerFirstName")
    private String firstName;
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "username-index", attributeName = "trainerLastName")
    private String lastName;
    @DynamoDBRangeKey(attributeName = "isActive")
    private String active;
    @DynamoDBTypeConverted(converter = TrainingSessionMapper.class)
    private Set<TrainingSession> trainingSessions;


    public static class TrainingSessionMapper implements DynamoDBTypeConverter<Set<String>, Set<TrainingSession>> {

        ObjectMapper mapper = new ObjectMapper();

        @Override
        public Set<String> convert(Set<TrainingSession> trainingSessions) {
            Set<String> result = new HashSet<>();
            try {
                for (TrainingSession trainingSession
                        : trainingSessions){
                    result.add(mapper.writeValueAsString(trainingSession));
                }
                return result;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to convert training sessions to JSON", e);
            }
        }

        @Override
        public Set<TrainingSession> unconvert(Set<String> strings) {
            Set<TrainingSession> result = new HashSet<>();
            try {
                for (String string:strings) {
                    result.add(mapper.readValue(string, TrainingSession.class));
                }
                return result;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Unable to convert JSON to training sessions", e);
            }
        }
    }
}