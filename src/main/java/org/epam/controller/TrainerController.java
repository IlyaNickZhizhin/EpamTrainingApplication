package org.epam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/trainer")
@RequiredArgsConstructor
@Tag(name="Trainer controller", description = "for registration, updating, deleting, selecting trainer")
@Slf4j
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
    public ResponseEntity<RegistrationResponse> register(@RequestBody TrainerRegistrationRequest request) {
        log.info("Registering trainer with name" + request.getFirstName() + " " + request.getLastName());
        try {
            RegistrationResponse response = trainerService.create(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            log.error("Error while registering trainer", e);
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
        log.info("Changing password for" + request.getUsername() + "trainer");
        try {
            boolean result = trainerService.changePassword(request);
            log.info("Password of " + request.getUsername() + "changed successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while changing password for" + request.getUsername(), e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/active")
    @Operation(summary = "Activate or deactivate trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Activate or deactivate trainer details",
                    content = @Content(schema = @Schema(implementation = ActivateDeactivateRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer activated or deactivated",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username or password")
            })
    public ResponseEntity<Boolean> setActive(@RequestBody ActivateDeactivateRequest request) {
        log.info("Changing active status of trainer: " + request.getUsername());
        try {
            boolean result = trainerService.setActive(request);
            log.info("Active status of trainer: " + request.getUsername() + " changed successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while changing active status of trainer: " + request.getUsername(), e);
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
        log.info("Selecting trainer by username: " + username);
        try {
            TrainerProfileResponse response = trainerService.selectByUsername(username);
            log.info("Trainer " + username + " selected successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(new TrainerProfileResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update trainer profile details",
                    content = @Content(schema = @Schema(implementation = UpdateTrainerProfileRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile updated",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username")
            })
    public ResponseEntity<TrainerProfileResponse> update(@PathVariable String username, @RequestBody UpdateTrainerProfileRequest request) {
        log.info("Updating trainer profile: " + request.getUsername());
        try {
            TrainerProfileResponse response = trainerService.update(username, request);
            log.info("Trainer profile: " + request.getUsername() + " updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException e) {
            log.error("Error while updating trainer profile: " + request.getUsername(), e);
            return new ResponseEntity<>(new TrainerProfileResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}

