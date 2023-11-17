package org.epam.model.gymModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_types")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class TrainingType implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_name", nullable = false)
    private TrainingName trainingName;
    public TrainingType(TrainingName trainingName) {
        this.trainingName = trainingName;
    }

    @Override
    public String getEntityName() {
        return "training_types";
    }

    public static enum TrainingName {
        BASIC,
        CARDIO,
        STRENGTH,
        WORKOUT,
        YOGA,
        PILATES,
        DANCE,
        BOXING,
        BODYBUILDING,
        CROSSFIT,
        AEROBICS,
        ZUMBA;
    }
}


