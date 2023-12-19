package org.epam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dto.trainingDto.*;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.gymModel.TrainingType;
import org.epam.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/api/training")
@RequiredArgsConstructor
@Tag(name = "Training controller", description = "for creating trainings, and other operations with" +
        " entities related to trainings")
@Slf4j
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping("/")
    @Operation(summary = "create training", description = "create training and assign trainee and trainer to it",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Training details",
            content = @Content(schema = @Schema(implementation = AddTrainingRequest.class))),
        responses = {@ApiResponse(responseCode = "201", description = "Training created",
            content = @Content(schema = @Schema(implementation = AddTrainingRequest.class)))})
    public ResponseEntity<AddTrainingRequest> create(@RequestBody AddTrainingRequest request) {
        log.info("Creating training with trainee" + " " + request.getTraineeUsername() + " " +
                "and trainer" + " " + request.getTrainerUsername() + " " + "and type" + " " + request.getTrainingType());
        try {
            AddTrainingRequest response = trainingService.create(request);
            log.info("Training with trainee " + request.getTraineeUsername() + " and trainer" +
                     request.getTrainerUsername() + " and type " + request.getTrainingType() + " created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            log.error("Error while creating training with trainee" + request.getTraineeUsername() + " and trainer " +
                    request.getTrainerUsername() + " and type " + request.getTrainingType() + " created successfully", e);
            return new ResponseEntity<>(new AddTrainingRequest(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/types")
    @Operation(summary = "get all training types", description = "get all training types",
        responses = {@ApiResponse(responseCode = "200", description = "Training types received",
            content = @Content(schema = @Schema(implementation = GetTrainingTypesResponse.class)))})
    public ResponseEntity<GetTrainingTypesResponse> getAllTrainingTypes() {
        log.info("Getting all training types");
        try {
            GetTrainingTypesResponse response = trainingService.selectAllTrainingTypes();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while getting all training types", e);
            return new ResponseEntity<>(new GetTrainingTypesResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-trainers")
    @Operation(summary = "update trainee trainers list", description = "add new trainers to Trainee trainers list",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "list of trainers usernames",
            content = @Content(schema = @Schema(implementation = UpdateTraineeTrainerListRequest.class))),
            responses = {
                @ApiResponse(responseCode = "200", description = "Trainers list updated",
                    content = @Content(schema = @Schema(implementation = GetTrainersResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid username or trainer username")
    })
    public ResponseEntity<GetTrainersResponse> updateTrainersList(@RequestBody
                                                                      UpdateTraineeTrainerListRequest request) {
        log.info("Updating trainee "+ request.getTraineeUsername() + " trainers list");
        try {
            GetTrainersResponse response = trainingService.updateTrainersList(request);
            log.info("Trainee " + request.getTraineeUsername() + " Trainers list updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while updating trainee " + request.getTraineeUsername() + " trainers list", e);
            return new ResponseEntity<>(new GetTrainersResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/trainers")
    @Operation(summary = "get trainee trainers list", description = "get trainee trainers list",
        responses = {@ApiResponse(responseCode = "200", description = "Trainers list received",
            content = @Content(schema = @Schema(implementation = GetTrainersResponse.class)))})
    public ResponseEntity<GetTrainersResponse> getTrainersList(@PathVariable String username) {
        log.info("Getting trainee " + username + " trainers list");
        try {
            GetTrainersResponse response = trainingService.getTrainersList(username);
            log.info("Trainee " + username + " trainers list received successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while getting trainee " + username + " trainers list", e);
            return new ResponseEntity<>(new GetTrainersResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/available-trainers")
    @Operation(summary = "get trainee available trainers list", description = "get trainee available trainers list",
        responses = {@ApiResponse(responseCode = "200", description = "Available trainers list received",
            content = @Content(schema = @Schema(implementation = GetTrainersResponse.class)))})
    public ResponseEntity<GetTrainersResponse> getNotAssignedOnTraineeActiveTrainers(@PathVariable String username) {
        log.info("Getting trainee " + username + " available trainers list");
        try {
            GetTrainersResponse response = trainingService.getNotAssignedOnTraineeActiveTrainers(username);
            log.info("Trainee " + username + " available trainers list received successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while getting trainee " + username + " available trainers list", e);
            return new ResponseEntity<>(new GetTrainersResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}

