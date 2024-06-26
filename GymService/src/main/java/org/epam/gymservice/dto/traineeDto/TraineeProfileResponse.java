package org.epam.gymservice.dto.traineeDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.epam.gymservice.dto.trainerDto.TrainerDto;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class TraineeProfileResponse {
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDto> trainers;
}

