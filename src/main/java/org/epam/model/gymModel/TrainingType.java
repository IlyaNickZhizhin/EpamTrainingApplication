package org.epam.model.gymModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "training_types")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class TrainingType implements Model {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

        public static List<TrainingName> gelList() {
            return Arrays.asList(values());
        }
    }
}


