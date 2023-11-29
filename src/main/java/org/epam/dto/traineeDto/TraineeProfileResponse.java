package org.epam.dto.traineeDto;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.epam.dto.trainerDto.TrainerDto;

import java.time.LocalDate;
import java.util.List;

@Setter
@EqualsAndHashCode
public class TraineeProfileResponse {
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDto> trainers;
}

