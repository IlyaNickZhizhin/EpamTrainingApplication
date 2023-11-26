package org.epam.dto.traineeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.epam.dto.trainingDto.TrainingDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class TraineeDto {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private boolean isActive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String address;
    private List<TrainingDto> trainings;
}

