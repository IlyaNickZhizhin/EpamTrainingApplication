package org.epam.model.gymModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * This class is used to store information about training types.
 * @see Model
 * @see TrainingType.TrainingName
 */

@Data
@Entity
@Table(name = "training_types")
public class TrainingType implements Model {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_name", nullable = false)
    @JsonProperty("training_name")
    private TrainingName trainingName;
    public TrainingType(TrainingName trainingName) {
        this.trainingName = trainingName;
    }

    /**
     * This enum is used to store information about training names.
     */
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


