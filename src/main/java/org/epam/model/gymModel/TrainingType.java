package org.epam.model.gymModel;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    public static TrainingType of(String trainingName) {
        return new TrainingType(TrainingName.idOf(trainingName), TrainingName.valueOf(trainingName));
    }

    public static TrainingType of(TrainingName trainingName) {
        return new TrainingType(TrainingName.idOf(trainingName), trainingName);
    }

    public enum TrainingName {
        BASIC, CARDIO, STRENGTH, WORKOUT, YOGA, PILATES, DANCE, BOXING, BODYBUILDING, CROSSFIT, AEROBICS, ZUMBA;

        public static int idOf(String name){
            return getTrainingNames().indexOf(TrainingName.valueOf(name))+1;
        }
        public static int idOf(TrainingName name){
            return getTrainingNames().indexOf(name)+1;
        }
        public static List<TrainingName> getTrainingNames() {
            return Arrays.asList(TrainingName.values());
        }
    }
}



