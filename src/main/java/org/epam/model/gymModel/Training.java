package org.epam.model.gymModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Training implements Model {
    private int id;

    private int traineeId;

    private int trainerId;

    private String trainingName;

    private int trainingTypeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date trainingDate;

    private Number duration;

}
