package org.epam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.epam.dto.trainingDto.AddTrainingRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainersResponse;
import org.epam.dto.trainingDto.GetTrainingTypesResponse;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.dto.trainingDto.UpdateTraineeTrainerListRequest;
import org.epam.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/training")
@RequiredArgsConstructor
@Tag(name = "Training controller", description = "for creating trainings, and other operations with" +
        " entities related to trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping("/")
    @Operation(summary = "create training", description = "create training and assign trainee and trainer to it",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Training details",
            content = @Content(schema = @Schema(implementation = AddTrainingRequest.class))),
        responses = {@ApiResponse(responseCode = "201", description = "Training created",
            content = @Content(schema = @Schema(implementation = AddTrainingRequest.class)))})
    public ResponseEntity<AddTrainingRequest> create(@RequestBody AddTrainingRequest request) {
        AddTrainingRequest response = trainingService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/types")
    @Operation(summary = "get all training types", description = "get all training types",
        responses = {@ApiResponse(responseCode = "200", description = "Training types received",
            content = @Content(schema = @Schema(implementation = GetTrainingTypesResponse.class)))})
    public ResponseEntity<GetTrainingTypesResponse> getAllTrainingTypes() {
        GetTrainingTypesResponse response = trainingService.selectAllTrainingTypes();
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        GetTrainersResponse response = trainingService.updateTrainersList(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/trainers")
    @Operation(summary = "get trainee trainers list", description = "get trainee trainers list",
        responses = {@ApiResponse(responseCode = "200", description = "Trainers list received",
            content = @Content(schema = @Schema(implementation = GetTrainersResponse.class)))})
    public ResponseEntity<GetTrainersResponse> getTrainersList(@PathVariable String username) {
        GetTrainersResponse response = trainingService.getTrainersList(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/available-trainers")
    @Operation(summary = "get trainee available trainers list", description = "get trainee available trainers list",
        responses = {@ApiResponse(responseCode = "200", description = "Available trainers list received",
            content = @Content(schema = @Schema(implementation = GetTrainersResponse.class)))})
    public ResponseEntity<GetTrainersResponse> getNotAssignedOnTraineeActiveTrainers(@PathVariable String username) {
        GetTrainersResponse response = trainingService.getNotAssignedOnTraineeActiveTrainers(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/trainee-trainings")
    @Operation(summary = "get trainings list for trainee", description = "get trainings list for trainee",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
                "specific parameters of training (Date and types)",
        content = @Content(schema = @Schema(implementation = GetTraineeTrainingsListRequest.class))),
    responses = {@ApiResponse(responseCode = "200", description = "Trainings list received",
            content = @Content(schema = @Schema(implementation = GetTrainingsResponse.class)))})
    public ResponseEntity<GetTrainingsResponse> getTraineeTrainingsList(@RequestBody
                                                                            GetTraineeTrainingsListRequest request) {
        GetTrainingsResponse response = trainingService.getTraineeTrainingsList(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/trainer-trainings")
    @Operation(summary = "get trainings list for trainer", description = "get trainings list for trainer",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description =
                "specific parameters of training (Date and types)",
        content = @Content(schema = @Schema(implementation = GetTrainerTrainingsListRequest.class))),
    responses = {@ApiResponse(responseCode = "200", description = "Trainings list received",
            content = @Content(schema = @Schema(implementation = GetTrainingsResponse.class)))})
    public ResponseEntity<GetTrainingsResponse> getTrainerTrainingsList(@RequestBody
                                                                            GetTrainerTrainingsListRequest request) {
        GetTrainingsResponse response = trainingService.getTrainerTrainingsList(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

