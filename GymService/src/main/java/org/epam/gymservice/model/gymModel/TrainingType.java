package org.epam.gymservice.model.gymModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



