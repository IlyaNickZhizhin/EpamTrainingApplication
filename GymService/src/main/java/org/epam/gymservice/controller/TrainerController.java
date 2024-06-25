package org.epam.gymservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.gymservice.dto.ActivateDeactivateRequest;
import org.epam.gymservice.dto.ChangeLoginRequest;
import org.epam.gymservice.dto.RegistrationResponse;
import org.epam.gymservice.dto.trainerDto.TrainerProfileResponse;
import org.epam.gymservice.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.gymservice.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainerTrainingsListRequest;
import org.epam.gymservice.dto.trainingDto.GetTrainingsResponse;
import org.epam.gymservice.exceptions.InvalidDataException;
import org.epam.gymservice.exceptions.ProhibitedActionException;
import org.epam.gymservice.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/api/trainer")
@RequiredArgsConstructor
@Tag(name="Trainer controller", description = "for registration, updating, deleting, selecting trainer")
@Slf4j
@Validated
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping("/")
    @Operation(summary = "register trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Trainer registration details",
                    content = @Content(schema = @Schema(implementation = TrainerRegistrationRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Trainer registered",
                            content = @Content(schema = @Schema(implementation = RegistrationResponse.class)))
            })
    @SecurityRequirements
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody TrainerRegistrationRequest request) {
        log.info("Registering trainer in " + getClass().getSimpleName());
        RegistrationResponse response = trainerService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/password")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN') or #request.username == authentication.principal.username")
    @Operation(summary = "Change password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Change password details",
                    content = @Content(schema = @Schema(implementation = ChangeLoginRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username or password")
            })
    public ResponseEntity<Boolean> changePassword(@Valid @RequestBody ChangeLoginRequest request) {
        log.info("Changing password for trainer in " + getClass().getSimpleName());
        try {
            boolean result = trainerService.changePassword(request);
            log.info("Password changed successfully in " + getClass().getSimpleName());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException | ProhibitedActionException e) {
            log.error("Error while changing password", e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/active")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN') or #request.username == authentication.principal.username")
    @Operation(summary = "Activate or deactivate trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Activate or deactivate trainer details",
                    content = @Content(schema = @Schema(implementation = ActivateDeactivateRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer activated or deactivated",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username or password")
            })
    public ResponseEntity<Boolean> setActive(@RequestBody ActivateDeactivateRequest request) {
        log.info("Changing active status in " + getClass().getSimpleName());
        try {
            boolean result = trainerService.setActive(request);
            log.info("Active status of trainer in requested condition" + request.isActive());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while changing active status of trainer", e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}")
    @Operation(summary = "Select trainer by username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer selected",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username")
            })
    public ResponseEntity<TrainerProfileResponse> selectByUsername(@PathVariable String username) {
        log.info("Selecting trainer by username: " + username.substring(0,0) + ".");
        try {
            TrainerProfileResponse response = trainerService.selectByUsername(username);
            log.info("Trainer username: " + username.substring(0,0) + ". selected successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new TrainerProfileResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN') or #request.username == authentication.principal.username")
    @Operation(summary = "Update trainer profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update trainer profile details",
                    content = @Content(schema = @Schema(implementation = UpdateTrainerProfileRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile updated",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username")
            })
    public ResponseEntity<TrainerProfileResponse> update(@Valid @RequestBody UpdateTrainerProfileRequest request) {
        log.info("Updating trainer profile in " + getClass().getSimpleName());
        try {
            TrainerProfileResponse response = trainerService.update(request);
            log.info("Trainer profile in" + getClass().getSimpleName() + " updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while updating trainer profile", e);
            return new ResponseEntity<>(new TrainerProfileResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}/trainings")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN') or #username == authentication.principal.username")
    @Operation(summary = "get trainings list for trainer", description = "get trainings list for trainer",
            parameters = {
                    @Parameter(name = "username", required = true, description = "The username"),
                    @Parameter(name = "periodFrom", description = "The start of the period"),
                    @Parameter(name = "periodTo", description = "The end of the period"),
                    @Parameter(name = "traineeName", description = "The name of the trainee")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainings list received",
                            content = @Content(schema = @Schema(implementation = GetTrainingsResponse.class)))
            }
    )
    public ResponseEntity<GetTrainingsResponse> getTrainerTrainingsList(@PathVariable String username,
                                                                        @RequestParam (required = false)
                                                                        LocalDate periodFrom,
                                                                        @RequestParam (required = false)
                                                                        LocalDate periodTo,
                                                                        @RequestParam (required = false)
                                                                        String traineeName) {
        log.info("Getting trainer trainings list");
        try {
            GetTrainerTrainingsListRequest request = new GetTrainerTrainingsListRequest();
            request.setPeriodFrom(periodFrom); request.setPeriodTo(periodTo); request.setTraineeName(traineeName);
            GetTrainingsResponse response = trainerService.getTrainerTrainingsList(username, request);
            log.info("Trainer trainings list received successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while getting trainer trainings list", e);
            return new ResponseEntity<>(new GetTrainingsResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}

