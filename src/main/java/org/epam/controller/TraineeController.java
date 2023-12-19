package org.epam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
import org.epam.dto.trainingDto.GetTraineeTrainingsListRequest;
import org.epam.dto.trainingDto.GetTrainingsResponse;
import org.epam.exceptions.InvalidDataException;
import org.epam.model.gymModel.TrainingType;
import org.epam.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/api/trainee")
@RequiredArgsConstructor
@Tag(name = "Trainee controller", description = "for registration, updating, deleting, selecting trainee")
@Slf4j
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping("/")
    @Operation(summary = "register trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trainee registration details",
                content = @Content(schema = @Schema(implementation = TraineeRegistrationRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Trainee registered",
                            content = @Content(schema = @Schema(implementation = RegistrationResponse.class)))
            })
    public ResponseEntity<RegistrationResponse> register(@RequestBody TraineeRegistrationRequest request) {
        log.info("Registering trainee with name" + request.getFirstname() + " " + request.getLastname());
        try {
            RegistrationResponse response = traineeService.create(request);
            log.info("Trainee registered successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e){
            log.error("Error while registering trainee", e);
            return new ResponseEntity<>(new RegistrationResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/password")
    @Operation(summary = "Change password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Change password details",
                    content = @Content(schema = @Schema(implementation = ChangeLoginRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username or password")
            })
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeLoginRequest request) {
        log.info("request for change password of trainee: " + request.getUsername());
        try {
            boolean result = traineeService.changePassword(request);
            log.info("Password of trainee: " + request.getUsername() + " changed successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error changing password of trainee: " + request.getUsername() + " " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/active")
    @Operation(summary = "Activate or deactivate trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Activation or deactivation details",
                    content = @Content(schema = @Schema(implementation = ActivateDeactivateRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee activated or deactivated successfully",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username")
            })
    public ResponseEntity<Boolean> setActive(@RequestBody ActivateDeactivateRequest request) {
        log.info("request for change active status of trainee: " + request.getUsername());
        try {
            boolean result = traineeService.setActive(request);
            log.info("Trainee: " + request.getUsername() + " active status changed successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error changing active status of trainee: " + request.getUsername() + " " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile",
            parameters = {
                    @Parameter(name = "username", in = ParameterIn.PATH, description = "Username of the trainee", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trainee profile details to update"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile updated successfully",
                            content = @Content(schema = @Schema(implementation = TraineeProfileResponse.class)))
            })
    public ResponseEntity<TraineeProfileResponse> update(@PathVariable String username, @RequestBody UpdateTraineeProfileRequest request) {
        log.info("request for update trainee profile: " + request.getUsername());
        try {
            TraineeProfileResponse response = traineeService.update(username, request);
            log.info("Trainee profile: " + request.getUsername() + " updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(InvalidDataException e){
            log.error("Error updating trainee profile: " + request.getUsername() + " " + e.getMessage());
            return new ResponseEntity<>(new TraineeProfileResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile by username",
            parameters = {
                    @Parameter(name = "username", in = ParameterIn.PATH, description = "Username of the trainee", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile retrieved successfully",
                            content = @Content(schema = @Schema(implementation = TraineeProfileResponse.class)))
            })
    public ResponseEntity<TraineeProfileResponse> selectByUsername(@PathVariable String username) {
        log.info("request for select trainee profile: " + username);
        try {
            TraineeProfileResponse response = traineeService.selectByUsername(username);
            log.info("Trainee profile: " + username + " retrieved successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error retrieving trainee profile: " + username + " " + e.getMessage());
            return new ResponseEntity<>(new TraineeProfileResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile",
            parameters = {
                    @Parameter(name = "username", in = ParameterIn.PATH, description = "Username of the trainee", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee profile deleted successfully",
                            content = @Content(schema = @Schema(implementation = Boolean.class)))
            })
    public ResponseEntity<Boolean> delete(@PathVariable String username) {
        log.info("request for delete trainee profile: " + username);
        try {
            boolean result = traineeService.delete(username);
            log.info("Trainee profile: " + username + " deleted successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error deleting trainee profile: " + username + " " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "get trainings list for trainee", description = "get trainings list for trainee",
            parameters = {
                    @Parameter(name = "username", required = true, description = "The username"),
                    @Parameter(name = "periodFrom", required = false, description = "The start of the period"),
                    @Parameter(name = "periodTo", required = false, description = "The end of the period"),
                    @Parameter(name = "trainerName", required = false, description = "The name of the trainer"),
                    @Parameter(name = "trainingName", required = false, description = "Type of training")
            },
            responses = {@ApiResponse(responseCode = "200", description = "Trainings list received",
                    content = @Content(schema = @Schema(implementation = GetTrainingsResponse.class)))})
    public ResponseEntity<GetTrainingsResponse> getTraineeTrainingsList(@PathVariable String username,
                                                                        @RequestParam (required = false)
                                                                        LocalDate periodFrom,
                                                                        @RequestParam (required = false)
                                                                        LocalDate periodTo,
                                                                        @RequestParam (required = false)
                                                                        String trainerName,
                                                                        @RequestParam (required = false)
                                                                        TrainingType.TrainingName trainingName) {
        log.info("Getting trainee " + username + " trainings list");
        try {
            GetTraineeTrainingsListRequest request = new GetTraineeTrainingsListRequest();
            request.setTrainingType(trainingName); request.setTrainerName(trainerName);
            request.setPeriodFrom(periodFrom); request.setPeriodTo(periodTo);
            GetTrainingsResponse response = traineeService.getTraineeTrainingsList(username, request);
            log.info("Trainee " + username + " trainings list received successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while getting trainee " + username + " trainings list", e);
            return new ResponseEntity<>(new GetTrainingsResponse(), HttpStatus.BAD_REQUEST);
        }
    }

}

