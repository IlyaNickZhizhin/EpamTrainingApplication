package org.epam.repository;

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

}
