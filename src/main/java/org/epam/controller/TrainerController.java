package org.epam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.trainerDto.TrainerProfileResponse;
import org.epam.dto.trainerDto.TrainerRegistrationRequest;
import org.epam.dto.trainerDto.UpdateTrainerProfileRequest;
import org.epam.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/trainer")
@RequiredArgsConstructor
@Tag(name="Trainer controller", description = "for registration, updating, deleting, selecting trainer")
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
        RegistrationResponse response = trainerService.create(request);
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
        boolean result = trainerService.changePassword(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
        boolean result = trainerService.setActive(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Select trainer by username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer selected",
                            content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username")
            })
    public ResponseEntity<TrainerProfileResponse> selectByUsername(@PathVariable String username) {
        TrainerProfileResponse response = trainerService.selectByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<TrainerProfileResponse> update(@RequestBody UpdateTrainerProfileRequest request) {
        TrainerProfileResponse response = trainerService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

