package org.epam.dto.traineeDto;

import lombok.Data;
import org.epam.dto.trainerDto.ShortTrainerDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class TraineeProfileResponse {
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<ShortTrainerDto> trainers;
}

