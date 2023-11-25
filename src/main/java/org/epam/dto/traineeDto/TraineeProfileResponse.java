package org.epam.dto.traineeDto;

import org.epam.dto.trainerDto.TrainerDto;

import java.time.LocalDate;
import java.util.List;

public class TraineeProfileResponse {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDto> trainers;
}

