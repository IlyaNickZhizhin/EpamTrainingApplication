package org.epam.mainservice.dto.traineeDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@EqualsAndHashCode
public class TraineeDto {
    private String firstname;
    private String lastname;
    private String username;
}
