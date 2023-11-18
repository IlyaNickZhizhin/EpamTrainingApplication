package org.epam.model.gymModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "trainings")
@ToString
public class Training implements Model {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "duration", nullable = false)
    private Number duration;

    @Override
    public String getEntityName() {
        return "trainings";
    }
}
