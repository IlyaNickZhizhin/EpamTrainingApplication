package org.epam.model.gymModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "training_name", nullable = false)
    @JsonProperty("training_name")
    private TrainingName trainingName;

    public enum TrainingName {
        BASIC, CARDIO, STRENGTH, WORKOUT, YOGA, PILATES, DANCE, BOXING, BODYBUILDING, CROSSFIT, AEROBICS, ZUMBA;

        public static List<TrainingName> getTrainingNames() {
            return Arrays.asList(TrainingName.values());
        }
    }
}



