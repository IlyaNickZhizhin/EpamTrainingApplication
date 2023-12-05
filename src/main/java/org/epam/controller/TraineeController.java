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
import org.epam.exceptions.InvalidDataException;
import org.epam.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        RegistrationResponse response = traineeService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
        boolean result = traineeService.changePassword(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
    public ResponseEntity<TraineeProfileResponse> update(@RequestBody UpdateTraineeProfileRequest request) {
        TraineeProfileResponse response = traineeService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        TraineeProfileResponse response = traineeService.selectByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        boolean result = traineeService.delete(username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

