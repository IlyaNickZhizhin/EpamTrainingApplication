package org.epam.mainservice.repository;

import org.epam.mainservice.model.gymModel.Training;
import org.epam.mainservice.model.gymModel.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query("SELECT t FROM TrainingType t")
    List<TrainingType> getAllTrainingTypes();

}
