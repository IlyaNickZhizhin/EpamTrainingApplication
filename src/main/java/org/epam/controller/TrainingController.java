package org.epam.controller;

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
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping("/types")
    public ResponseEntity<GetTrainingTypesResponse> getAllTrainingTypes() {
        GetTrainingTypesResponse response = trainingService.selectAllTrainingTypes();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AddTrainingRequest> create(@RequestBody AddTrainingRequest request) {
        AddTrainingRequest response = trainingService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update-trainers")
    public ResponseEntity<GetTrainersResponse> updateTrainersList(@RequestBody
                                                                      UpdateTraineeTrainerListRequest request) {
        GetTrainersResponse response = trainingService.updateTrainersList(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/trainers/{username}")
    public ResponseEntity<GetTrainersResponse> getTrainersList(@PathVariable String username) {
        GetTrainersResponse response = trainingService.getTrainersList(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/available-trainers/{username}")
    public ResponseEntity<GetTrainersResponse> getNotAssignedOnTraineeActiveTrainers(@PathVariable String username) {
        GetTrainersResponse response = trainingService.getNotAssignedOnTraineeActiveTrainers(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/trainee-trainings")
    public ResponseEntity<GetTrainingsResponse> getTraineeTrainingsList(@RequestBody
                                                                            GetTraineeTrainingsListRequest request) {
        GetTrainingsResponse response = trainingService.getTraineeTrainingsList(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/trainer-trainings")
    public ResponseEntity<GetTrainingsResponse> getTrainerTrainingsList(@RequestBody
                                                                            GetTrainerTrainingsListRequest request) {
        GetTrainingsResponse response = trainingService.getTrainerTrainingsList(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

