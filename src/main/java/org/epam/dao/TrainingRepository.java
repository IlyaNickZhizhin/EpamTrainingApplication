package org.epam.dao;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query("SELECT t FROM TrainingType t")
    List<TrainingType> getAllTrainingTypes();

    @Query("SELECT t FROM Training t WHERE t.trainee = :trainee AND t.trainingType IN :types")
    List<Training> getAllByUsernameAndTrainingTypes(List<TrainingType> types, Trainee trainee);

    @Query("SELECT t FROM Training t WHERE t.trainer = :trainer AND t.trainingType IN :types")
    List<Training> getAllByUsernameAndTrainingTypes(List<TrainingType> types, Trainer trainer);

    @Query("SELECT t FROM Training t WHERE t.trainee = :trainee AND t.trainer NOT IN :trainers")
    List<Trainer> getAllTrainersAvailableForTrainee(Trainee trainee, List<Trainer> trainers);

}
