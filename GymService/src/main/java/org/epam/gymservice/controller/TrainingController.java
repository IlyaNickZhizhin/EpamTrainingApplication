package org.epam.gymservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.gymservice.dto.trainingDto.AddTrainingRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainersResponse;
import org.epam.gymservice.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.gymservice.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/training")
@RequiredArgsConstructor
@Tag(name = "Training controller", description = "for creating trainings, and other operations with" +
        " entities related to trainings")
@Slf4j
@CrossOrigin
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or " +
            "hasAuthority('ROLE_TRAINER') and #request.trainerUsername == principal.username " +
            "or hasAuthority('ROLE_TRAINEE') and #request.traineeUsername == principal.username")
    @Operation(summary = "create training", description = "create training and assign trainee and trainer to it",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Training details",
            content = @Content(schema = @Schema(implementation = AddTrainingRequest.class))),
        responses = {@ApiResponse(responseCode = "201", description = "Training created",
            content = @Content(schema = @Schema(implementation = AddTrainingRequest.class)))})
    public ResponseEntity<AddTrainingRequest> create(@RequestBody AddTrainingRequest request) {
        log.info("Creating " + request.getTrainingType().name() + " training with name: " + request.getTrainingName() +
                " on " + request.getTrainingDate() + " at " + request.getTrainingDuration());
        try {
            AddTrainingRequest response = trainingService.create(request);
            log.info(request.getTrainingType().name() + " training with name: " + request.getTrainingName() +
            " on " + request.getTrainingDate() + " at " + request.getTrainingDuration() + " created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            log.error("Error while creating " + request.getTrainingType().name() + " training with name: " + request.getTrainingName(), e);
            return new ResponseEntity<>(new AddTrainingRequest(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("#request.traineeUsername == principal.username or hasAuthority('ROLE_ADMIN')")
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
        log.info("Updating trainee trainers list in" + getClass().getSimpleName());
        try {
            GetTrainersResponse response = trainingService.updateTrainersList(request);
            log.info("Trainee trainers list updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while updating trainee trainers list", e);
            return new ResponseEntity<>(new GetTrainersResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/trainers")
    @PreAuthorize("#username == principal.username or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "get trainee trainers list", description = "get trainee trainers list",
        responses = {@ApiResponse(responseCode = "200", description = "Trainers list received",
            content = @Content(schema = @Schema(implementation = GetTrainersResponse.class)))})
    public ResponseEntity<GetTrainersResponse> getTrainersList(@PathVariable String username) {
        log.info("Getting trainee trainers list");
        try {
            GetTrainersResponse response = trainingService.getTrainersList(username);
            log.info("Trainee trainers list received successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while getting trainee trainers list", e);
            return new ResponseEntity<>(new GetTrainersResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/available-trainers")
    @PreAuthorize("#username == principal.username or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "get trainee available trainers list", description = "get trainee available trainers list",
        responses = {@ApiResponse(responseCode = "200", description = "Available trainers list received",
            content = @Content(schema = @Schema(implementation = GetTrainersResponse.class)))})
    public ResponseEntity<GetTrainersResponse> getNotAssignedOnTraineeActiveTrainers(@PathVariable String username) {
        log.info("Getting trainee available trainers list");
        try {
            GetTrainersResponse response = trainingService.getNotAssignedOnTraineeActiveTrainers(username);
            log.info("Trainee available trainers list received successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while getting trainee available trainers list", e);
            return new ResponseEntity<>(new GetTrainersResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}

