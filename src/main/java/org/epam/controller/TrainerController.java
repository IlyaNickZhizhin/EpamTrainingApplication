package org.epam.controller;

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
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody TrainerRegistrationRequest request) {
        RegistrationResponse response = trainerService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeLoginRequest request) {
        boolean result = trainerService.changePassword(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/select/{username}")
    public ResponseEntity<TrainerProfileResponse> selectByUsername(@PathVariable String username) {
        TrainerProfileResponse response = trainerService.selectByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TrainerProfileResponse> update(@RequestBody UpdateTrainerProfileRequest request) {
        TrainerProfileResponse response = trainerService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/set-active")
    public ResponseEntity<Boolean> setActive(@RequestBody ActivateDeactivateRequest request) {
        boolean result = trainerService.setActive(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

