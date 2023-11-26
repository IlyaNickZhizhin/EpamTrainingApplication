package org.epam.dto.traineeDto;

import lombok.Data;
import org.epam.dto.trainerDto.ShotTrainerDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class TraineeProfileResponse {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<ShotTrainerDto> trainers;
}

