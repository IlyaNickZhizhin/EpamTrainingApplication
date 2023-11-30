package org.epam.controller;

import lombok.RequiredArgsConstructor;
import org.epam.dto.ActivateDeactivateRequest;
import org.epam.dto.ChangeLoginRequest;
import org.epam.dto.RegistrationResponse;
import org.epam.dto.traineeDto.TraineeProfileResponse;
import org.epam.dto.traineeDto.TraineeRegistrationRequest;
import org.epam.dto.traineeDto.UpdateTraineeProfileRequest;
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
@RequestMapping("/api/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody TraineeRegistrationRequest request) {
        RegistrationResponse response = traineeService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeLoginRequest request) {
        boolean result = traineeService.changePassword(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> selectByUsername(@PathVariable String username) {
        TraineeProfileResponse response = traineeService.selectByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TraineeProfileResponse> update(@RequestBody UpdateTraineeProfileRequest request) {
        TraineeProfileResponse response = traineeService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Boolean> delete(@PathVariable String username) {
        boolean result = traineeService.delete(username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/set-active")
    public ResponseEntity<Boolean> setActive(@RequestBody ActivateDeactivateRequest request) {
        boolean result = traineeService.setActive(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

