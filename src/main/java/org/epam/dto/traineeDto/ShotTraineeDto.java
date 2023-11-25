package org.epam.dto.traineeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.epam.dto.trainingDto.TrainingDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class ShotTraineeDto {
    private String firstname;
    private String lastname;
    private String username;
}

